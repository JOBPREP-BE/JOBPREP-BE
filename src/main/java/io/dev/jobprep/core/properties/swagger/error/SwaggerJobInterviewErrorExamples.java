package io.dev.jobprep.core.properties.swagger.error;

public class SwaggerJobInterviewErrorExamples {
    public static final String ALREADY_DELETED_INTERVIEW = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-JOB-INTERVIEW-001\",\"message\":\"해당 면접 데이터는 이미 삭제되었습니다.\"}";
    public static final String INTERVIEW_FORBIDDEN_OPERATION = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-JOB-INTERVIEW-001\",\"message\":\"해당 작업은 작성자 권한이 필요합니다.\"}";
    public static final String INTERVIEW_NOT_FOUND = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":404,\"code\":\"E03-JOB-INTERVIEW-001\",\"message\":\"존재하지 않는 면접 데이터입니다.\"}";
    public static final String IS_DEFAULT_INTERVIEW = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-JOB-INTERVIEW-003\",\"message\":\"해당 면접은 유저가 변경할 수 없습니다.\"}";
}
