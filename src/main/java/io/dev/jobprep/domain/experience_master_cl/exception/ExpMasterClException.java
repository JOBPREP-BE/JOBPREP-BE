package io.dev.jobprep.domain.experience_master_cl.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class ExpMasterClException extends CustomException {
    public ExpMasterClException(ErrorCode errorCode) {
        super(errorCode);
    }
}
