package io.dev.jobprep.core.properties.swagger.error;

public class SwaggerUserErrorExamples {

    public static final String USER_NOT_FOUND = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":404,\"code\":\"E03-USER-001\",\"message\":\"존재하지 않는 유저입니다.\"}";
    public static final String USER_ACCOUNT_DISABLED = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":401,\"code\":\"E01-USER-001\",\"message\":\"탈퇴한 유저입니다\"}";
    public static final String USER_ACCOUNT_ALREADY_EXISTS = "{\"timestamp\":\"2024-02-17T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-USER-001\",\"message\":\"해당 이메일로 가입된 계정이 이미 존재합니다.\"}";

}
