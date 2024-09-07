package edu.wgu.d387_sample_code.rest;

import edu.wgu.d387_sample_code.model.response.StringResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

    @RestController
    @CrossOrigin
    public class TimeZoneController {
        @RequestMapping(path = "time", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public StringResponse getTime() {
            ZoneId ET = ZoneId.of("US/Eastern");
            ZoneId MT = ZoneId.of("US/Mountain");
            ZoneId CT = ZoneId.of("US/Central");
            ZoneId UTC = ZoneId.of("UTC");
            ZonedDateTime zonedTime = ZonedDateTime.parse("2023-10-13T10:30:00.00+00:00");
            DateTimeFormatter dateDtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            String date = zonedTime.format(dateDtf);
            String eastern = zonedTime.withZoneSameInstant(ET).toLocalTime().format(dtf).toString();
            String mountain = zonedTime.withZoneSameInstant(MT).toLocalTime().format(dtf).toString();
            String utc = zonedTime.withZoneSameInstant(UTC).toLocalTime().format(dtf).toString();

            return new StringResponse(date + " " + eastern + "ET | " + mountain + "MT | " + utc + "UTC");
        }
    }
