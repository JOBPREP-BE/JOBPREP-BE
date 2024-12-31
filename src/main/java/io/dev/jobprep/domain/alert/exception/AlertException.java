package io.dev.jobprep.domain.alert.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class AlertException extends CustomException {

    public AlertException(ErrorCode errorCode) {
        super(errorCode);
    }
}
