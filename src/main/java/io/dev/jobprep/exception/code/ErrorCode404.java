package io.dev.jobprep.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode404 implements ErrorCode {

    STUDY_NOT_FOUND("E03-STUDY-001", "존재하지 않는 스터디입니다."),


    USER_NOT_FOUND("E03-USER-001", "존재하지 않는 유저입니다."),
    ADMIN_NOT_FOUND("E03-USER-002", "관리자를 찾을 수 없습니다."),

    INTERVIEW_NOT_FOUND("E03-JOB-INTERVIEW-001", "존재하지 않는 면접 데이터입니다."),

    APPLICATION_STATUS_NOT_FOUND("E03-APPLICATIONSTATUS-001", "해당 지원 현황이 존재하지 않습니다."),


    ESSENTIAL_MATERIAL_NOT_FOUND("E03-ESSENTIALMATERIAL-001", "해당 자원이 존재하지 않습니다")

    ;

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private final String code;
    private final String message;

    ErrorCode404(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
