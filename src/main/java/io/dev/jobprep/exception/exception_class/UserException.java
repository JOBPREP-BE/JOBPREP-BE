package io.dev.jobprep.exception.exception_class;

import io.dev.jobprep.exception.code.ErrorCode;

public class UserException extends CustomException{
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
