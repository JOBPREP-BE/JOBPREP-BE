package io.dev.jobprep.core.properties.swagger.error;

public class SwaggerJwtErrorExamples {
    public static final String AUTH_MISSING_CREDENTIALS = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":401,\"code\":\"E02-AUTH-001\",\"message\":\"사용자의 인증 정보를 찾을 수 없습니다\"}";
    public static final String AUTH_ACCESS_DENIED = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-AUTH-002\",\"message\":\"접근 권한이 없습니다.\"}";
    public static final String AUTH_TOKEN_EXPIRED = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":401,\"code\":\"E02-AUTH-003\",\"message\":\"토큰이 만료되었습니다.\"}";

    public static final String RFT_CACHE_FALIURE = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-JWT-001\",\"message\":\"리프레쉬 토큰 캐싱을 실패했습니다.\"}";
    public static final String RFT_CACHE_DELETION_FALIURE= "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-JWT-002\",\"message\":\"리프레쉬 토큰 캐싱을 실패했습니다.\"}";
    public static final String RFT_VALIDATION_FAILURE = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-JWT-003\",\"message\":\"리프레쉬 토큰 캐시 검증을 실패했습니다.\"}";
}
