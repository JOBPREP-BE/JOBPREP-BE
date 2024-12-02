package io.dev.jobprep.exception.exception_class;

import io.dev.jobprep.exception.code.ErrorCode;

public class JobInterviewException extends CustomException{
    public JobInterviewException(ErrorCode errorCode){
        super(errorCode);
    }
}
