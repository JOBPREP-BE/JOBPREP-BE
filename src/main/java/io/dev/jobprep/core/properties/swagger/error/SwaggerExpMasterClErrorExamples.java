package io.dev.jobprep.core.properties.swagger.error;

public class SwaggerExpMasterClErrorExamples {
    public static final String ALREADY_DELETED_MASTER_CL = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-EXP-MASTER-001\",\"message\":\"해당 자소서는 이미 삭제되었습니다.\"}";

    public static final String INVALID_PROCESS_MASTER_CL = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-EXP-MASTER-002\",\"message\":\"해당 자소서는 알맞은 과정이 아닙니다.\"}";

    public static final String INVALID_FIELD_MASTER_CL = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":400,\"code\":\"E01-EXP-MASTER-003\",\"message\":\"해당 필드는 마스터 자소서에 없습니다.\"}";

    public static final String MASTER_CL_FORBIDDEN_OPERATION = "{\"timestamp\":\"2024-12-02T10:07:31.404Z\",\"statusCode\":403,\"code\":\"E02-EXP-MASTER-001\",\"message\":\"해당 작업은 작성자 권한이 필요합니다.\"}";
}
