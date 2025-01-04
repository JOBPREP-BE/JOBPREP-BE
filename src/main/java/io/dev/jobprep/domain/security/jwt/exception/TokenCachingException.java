package io.dev.jobprep.domain.security.jwt.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class TokenCachingException extends CustomException {
    public TokenCachingException(ErrorCode errorCode) {
        super(errorCode);
    }
}