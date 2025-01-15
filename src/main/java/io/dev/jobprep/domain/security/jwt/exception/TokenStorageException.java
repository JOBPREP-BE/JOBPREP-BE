package io.dev.jobprep.domain.security.jwt.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

import javax.naming.AuthenticationException;

public class TokenStorageException extends CustomException {
    public TokenStorageException(ErrorCode errorCode){super(errorCode);}
}