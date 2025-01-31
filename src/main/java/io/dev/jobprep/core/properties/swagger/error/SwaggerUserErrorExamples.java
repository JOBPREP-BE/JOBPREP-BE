package io.dev.jobprep.core.properties.swagger.error;

public class SwaggerUserErrorExamples {

    public static final String USER_NOT_FOUND = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":404,\"code\":\"E03-USER-001\",\"message\":\"존재하지 않는 유저입니다.\"}";

    public static final String USER_ACCOUNT_DISABLED = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":401,\"code\":\"E01-USER-001\",\"message\":\"탈퇴한 유저입니다\"}";
    public static final String USER_ACCOUNT_ALREADY_EXISTS = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-USER-001\",\"message\":\"해당 이메일로 가입된 계정이 이미 존재합니다.\"}";
    public static final String ALREADY_PENALIZED_USER = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-USER-002\",\"message\":\"이미 페널티가 부여된 유저입니다.\"}";

    public static final String ADMIN_FORBIDDEN_OPERATION = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-USER-001\",\"message\":\"해당 작업은 관리자 권한이 필요합니다.\"}";
    public static final String USER_FORBIDDEN_OPERATION = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-USER-002\",\"message\":\"해당 작업에 대한 권한이 없습니다.\"}";
    public static final String USER_PERMISSION_SUSPENDED = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-USER-003\",\"message\":\"페널티로 인해 이용이 제한되었습니다.\"}";

}
