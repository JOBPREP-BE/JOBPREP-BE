package io.dev.jobprep.domain.security.jwt.exception;

import javax.naming.AuthenticationException;

public class TokenStorageException extends AuthenticationException {
    public TokenStorageException(String msg) {
        super(msg);
    }
}