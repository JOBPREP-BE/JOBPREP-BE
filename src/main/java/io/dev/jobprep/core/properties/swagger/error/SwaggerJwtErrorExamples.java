package io.dev.jobprep.core.properties.swagger.error;

public class SwaggerJwtErrorExamples {
    public static final String AUTH_MISSING_CREDENTIALS = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":401,\"code\":\"E02-AUTH-001\",\"message\":\"사용자의 인증 정보를 찾을 수 없습니다\"}";
    public static final String AUTH_ACCESS_DENIED = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-AUTH-002\",\"message\":\"접근 권한이 없습니다.\"}";
    public static final String AUTH_TOKEN_EXPIRED = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":401,\"code\":\"E02-AUTH-003\",\"message\":\"토큰이 만료되었습니다.\"}";

    public static final String REFRESH_TOKEN_CACHING_FAILED = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-JWT-001\",\"message\":\"리프레쉬 토큰 캐싱 과정에서 에러가 발생하였습니다.\"}";
    public static final String REFRESH_TOKEN_CACHE_DELETION_ERROR= "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-JWT-002\",\"message\":\"리프레쉬 토큰 캐시 삭제 과정에서 에러가 발생하였습니다\"}";
    public static final String REFRESH_TOKEN_CACHE_VALIDATION_FAILED= "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-JWT-003\",\"message\":\"리프레쉬 토큰 캐시 검증 과정에서 에러가 발생하였습니다.\"}";
}
