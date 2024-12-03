package io.dev.jobprep.domain.applicationstatus.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class ApplicationStatusException extends CustomException {

    public ApplicationStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
