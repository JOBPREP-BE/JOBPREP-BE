package io.dev.jobprep.system.internal.developer.token;

import lombok.Getter;

@Getter
public class DevToken {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final String token;

    private DevToken(final String token) {
        this.token = token;
    }

    public static DevToken of(String token) {
        token = token.substring(TOKEN_PREFIX.length());
        return new DevToken(token);
    }

    public boolean verify(String token) {
        return this.token.equals(token);
    }
}
