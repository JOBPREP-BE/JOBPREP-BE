package io.dev.jobprep.domain.study.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class StudyException extends CustomException {

    public StudyException(ErrorCode errorCode) {
        super(errorCode);
    }

}
