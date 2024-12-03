package io.dev.jobprep.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode401 implements ErrorCode {

    AUTH_MISSING_CREDENTIALS("E02-AUTH-001", "사용자의 인증 정보를 찾을 수 없습니다."),
    AUTH_TOKEN_EXPIRED("E02-AUTH-002", "토큰이 만료되었습니다."),

    USER_ACCOUNT_DISABLED("E01-USER-001", "탈퇴한 유저입니다")
    ;

    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    private final String code;
    private final String message;

    ErrorCode401(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
