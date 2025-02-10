package io.dev.jobprep.common.constants;

public class TokenHeaderConstants {

    public static final String AUTHENTICATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REFRESH_HEADER = "XRefreshToken";
    public static final String CSRF_HEADER = "X-CSRF-Token";
    public static final String OTP_HEADER = "otp_token";

    private TokenHeaderConstants() {
        throw new UnsupportedOperationException("Cannot instantiate this class!");
    }
}