package io.dev.jobprep.domain.chat.exception;

import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class ChatException extends CustomException {

    public ChatException(ErrorCode errorCode) {
        super(errorCode);
    }
}
