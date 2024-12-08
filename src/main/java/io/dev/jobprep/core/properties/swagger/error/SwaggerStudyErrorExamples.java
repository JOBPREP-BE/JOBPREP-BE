package io.dev.jobprep.core.properties.swagger.error;

public class SwaggerStudyErrorExamples {

    public static final String DUPLICATE_STUDY_NAME = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E00-STUDY-001\",\"message\":\"해당 스터디 이름이 이미 존재합니다.\"}";
    public static final String INVALID_STUDY_STATUS_TO_RECRUIT = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-STUDY-001\",\"message\":\"스터디가 현재 모집중이 아닙니다.\"}";
    public static final String STUDY_WEEK_NUMBER_EXCEED = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-STUDY-002\",\"message\":\"스터디는 3주 이상 진행할 수 없습니다.\"}";
    public static final String ALREADY_CREATED_STUDY = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-STUDY-003\",\"message\":\"스터디는 한 번에 하나만 생성할 수 있습니다.\"}";
    public static final String ALREADY_FINISHED_STUDY = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-STUDY-004\",\"message\":\"해당 스터디는 이미 종료되었습니다.\"}";
    public static final String ALREADY_DELETED_STUDY = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-STUDY-005\",\"message\":\"해당 스터디는 이미 삭제되었습니다.\"}";
    public static final String STUDY_GATHERED_USER_EXCEED = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-STUDY-006\",\"message\":\"해당 스터디의 모집 인원이 모두 채워졌습니다.\"}";
    public static final String ALREADY_GATHERED_STUDY = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-STUDY-007\",\"message\":\"이미 참여 중인 스터디가 있습니다.\"}";
    public static final String ALREADY_PASSED_DUE_DATE = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-STUDY-008\",\"message\":\"스터디의 모집 기간이 지났습니다.\"}";
    public static final String NON_GATHERED_USER = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-STUDY-011\",\"message\":\"해당 스터디에 유저가 존재하지 않습니다.\"}";
    public static final String STUDY_FORBIDDEN_OPERATION = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-STUDY-001\",\"message\":\"해당 작업에 대한 권한이 없습니다.\"}";
    public static final String STUDY_NOT_FOUND = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":404,\"code\":\"E03-STUDY-001\",\"message\":\"존재하지 않는 스터디입니다.\"}";
}
