package io.dev.jobprep.core.properties.swagger.error;

public class SwaggerUserErrorExamples {

    public static final String USER_NOT_FOUND = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":404,\"code\":\"E03-USER-001\",\"message\":\"존재하지 않는 유저입니다.\"}";
    public static final String ADMIN_FORBIDDEN_OPERATION = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-USER-001\",\"message\":\"해당 작업은 관리자 권한이 필요합니다.\"}";
    public static final String USER_FORBIDDEN_OPERATION = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-USER-002\",\"message\":\"해당 작업에 대한 권한이 없습니다.\"}";
}
