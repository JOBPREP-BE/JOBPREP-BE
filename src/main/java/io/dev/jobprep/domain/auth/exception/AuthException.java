package io.dev.jobprep.domain.auth.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
