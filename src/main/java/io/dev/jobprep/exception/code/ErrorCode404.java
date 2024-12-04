package io.dev.jobprep.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode404 implements ErrorCode {

    STUDY_NOT_FOUND("E03-STUDY-001", "존재하지 않는 스터디입니다."),

    USER_NOT_FOUND("E03-USER-002", "요청된 ID를 가진 유저가 존재하지 않습니다"),
    ;

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private final String code;
    private final String message;

    ErrorCode404(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
