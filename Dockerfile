FROM node:alpine
WORKDIR /src/main/UI
COPY package.json package-lock.json ./
RUN npm install -g @angular/cli
RUN npm install
CMD ["ng", "serve", "--host", "0.0.0.0"]
ARG JAR_FILE=/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]