package io.dev.jobprep.util;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeConverter {

    private static final String PATTERN = "Z";

    public static LocalDateTime convertToUtcLDT(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        try {
            if (!hasTimeZoneOrOffset(value)) {
                return LocalDateTime.parse(value, formatter);
            }
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(value, formatter);
            return offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
        } catch (DateTimeParseException e) {
            // TODO: 에러 핸들링
            throw new IllegalArgumentException("Invalid date-time format. Expected format: yyyy-MM-ddTHH:mm:ss.fffZ", e);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid date-time format. Expected format: yyyy-MM-ddTHH:mm:ss.fffZ", e);
        }
    }

    private static boolean hasTimeZoneOrOffset(String value) {
        return value.endsWith(PATTERN);
    }

}
