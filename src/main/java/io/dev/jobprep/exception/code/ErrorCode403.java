package io.dev.jobprep.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode403 implements ErrorCode {

    AUTH_ACCESS_DENIED("E02-AUTH-001", "접근 권한이 없습니다."),

    INTERVIEW_FORBIDDEN_OPERATION("E02-JOB-INTERVIEW-001", "해당 작업은 작성자 권한이 필요합니다."),

    STUDY_FORBIDDEN_OPERATION("E02-STUDY-001", "해당 작업에 대한 권한이 없습니다."),

    ADMIN_FORBIDDEN_OPERATION("E02-USER-001", "해당 작업은 관리자 권한이 필요합니다."),
    USER_FORBIDDEN_OPERATION("E02-USER-002", "해당 작업에 대한 권한이 없습니다."),
    USER_PERMISSION_SUSPENDED("E02-USER-003", "페널티로 인해 이용이 제한되었습니다."),

    APPLICATION_STATUS_FORBIDDEN_OPERATION("E02-APPLICATIONSTATUS-001", "해당 작업에 대한 권한이 없습니다."),

    MASTER_CL_FORBIDDEN_OPERATION("E02-EXP-MASTER-001", "해당 작업은 작성자 권한이 필요합니다."),

    CHAT_ROOM_FORBIDDEN_OPERATION("E02-CHATROOM-001", "해당 작업은 관리자 권한이 필요합니다."),
    ;

    private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    private final String code;
    private final String message;

    ErrorCode403(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
