# Stage 1: Dependency resolution
FROM eclipse-temurin:17-jdk-jammy AS deps
WORKDIR /build

# Copy Maven wrapper files
COPY mvnw .
COPY .mvn .mvn

# Ensure mvnw is executable
RUN chmod +x mvnw

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

# Stage 2: Build and package the application
FROM deps AS package
COPY src src
RUN --mount=type=cache,target=/root/.m2 ./mvnw package -DskipTests

# Stage 3: Extract the built application
FROM package AS extract
RUN java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

# Stage 4: Final runtime image
FROM eclipse-temurin:17-jre-jammy AS final
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

# Copy application layers
COPY --from=extract build/target/extracted/dependencies/ ./
COPY --from=extract build/target/extracted/spring-boot-loader/ ./
COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract build/target/extracted/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]