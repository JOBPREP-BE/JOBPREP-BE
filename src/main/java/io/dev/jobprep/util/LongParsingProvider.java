package io.dev.jobprep.util;

public class LongParsingProvider {

    public static Long provide(String source) {

        if (source == null || source.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(source);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Argument. You need to transmission 'Long' type!");
        }
    }

}
