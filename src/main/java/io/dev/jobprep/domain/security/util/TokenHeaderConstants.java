package io.dev.jobprep.domain.security.util;

public class TokenHeaderConstants {

    public static final String AUTHENTICATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REFRESH_HEADER = "XRefreshToken";

    private TokenHeaderConstants() {
        throw new UnsupportedOperationException("Cannot instantiate this class!");
    }
}