package io.dev.jobprep.domain.users.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class UserException extends CustomException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
