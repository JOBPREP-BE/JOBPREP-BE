package io.dev.jobprep.system.internal.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class InternalException extends CustomException {

    public InternalException(ErrorCode errorCode) {
        super(errorCode);
    }
}
