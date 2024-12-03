package io.dev.jobprep.util;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter {

    public static LocalDateTime convertToUtcLDT(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(value, formatter);
            return offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
        } catch (DateTimeException e) {
            // TODO: 에러 핸들링
            throw new IllegalArgumentException("Invalid date-time format. Expected format: yyyy-MM-ddTHH:mm:ss", e);
        }
    }

}
