package io.dev.jobprep.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode403 implements ErrorCode {

    AUTH_ACCESS_DENIED("E02-AUTH-001", "접근 권한이 없습니다."),
  
    STUDY_FORBIDDEN_OPERATION("E02-STUDY-001", "해당 작업은 관리자 권한이 필요합니다."),
    STUDY_PARTICIPANTS_FORBIDDEN_OPERATION("E02-STUDY-002", "해당 작업은 스터디 참여자만 가능합니다."),
    ;

    private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    private final String code;
    private final String message;

    ErrorCode403(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
