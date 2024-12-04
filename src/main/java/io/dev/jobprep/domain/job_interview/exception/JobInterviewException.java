package io.dev.jobprep.domain.job_interview.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class JobInterviewException extends CustomException {
    public JobInterviewException(ErrorCode errorCode){
        super(errorCode);
    }
}
