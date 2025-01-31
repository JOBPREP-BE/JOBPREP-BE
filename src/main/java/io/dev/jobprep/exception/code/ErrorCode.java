package io.dev.jobprep.exception.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();

}
