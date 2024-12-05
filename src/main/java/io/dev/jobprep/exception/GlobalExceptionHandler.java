package io.dev.jobprep.exception;

import static io.dev.jobprep.exception.code.ErrorCode400.ILLEGAL_INPUT_ARG;
import static io.dev.jobprep.exception.code.ErrorCode400.INVALID_INPUT_VALUE;
import static io.dev.jobprep.exception.code.ErrorCode400.PATH_PARAMETER_BAD_REQUEST;
import static io.dev.jobprep.exception.code.ErrorCode401.AUTH_MISSING_CREDENTIALS;
import static io.dev.jobprep.exception.code.ErrorCode401.AUTH_TOKEN_EXPIRED;
import static io.dev.jobprep.exception.code.ErrorCode403.AUTH_ACCESS_DENIED;
import static io.dev.jobprep.exception.code.ErrorCode500.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.dev.jobprep.exception.dto.ErrorResponse;
import io.dev.jobprep.exception.exception_class.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(
        CustomException e, HttpServletRequest request
    ) {
        log.info("Custom Exception: {}, Path: {}", e.getMessage(), request.getPathInfo());
        return ErrorResponse.from(e.getErrorCode());
    }

    @ExceptionHandler(value = {
        BindException.class,
        MethodArgumentNotValidException.class
    })
    protected ResponseEntity<ErrorResponse> validationException(
        BindException e, HttpServletRequest request
    ) {
        log.info("Validation Exception: {}, Path: {}", e.getMessage(), request.getPathInfo());
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("] (은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
            builder.append(", ");
        }
        log.info(builder.toString());

        return ErrorResponse.ofWithErrorMessage(INVALID_INPUT_VALUE, builder.toString());
    }

    @ExceptionHandler(value = {
        MissingPathVariableException.class,
        MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> missingPathVariableException(
        Exception e, HttpServletRequest request
    ) {
        log.info("Missing Path Variable Exception: {}, Path: {}", e.getMessage(), request.getPathInfo());
        return ErrorResponse.from(PATH_PARAMETER_BAD_REQUEST);
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    protected ResponseEntity<ErrorResponse> handleTokenExpiredException(
        TokenExpiredException e, HttpServletRequest request

    ) {
        log.info("Token Expiry Exception: {}, Path: {}", e.getMessage(), request.getPathInfo());
        return ErrorResponse.from(AUTH_TOKEN_EXPIRED);
    }

    @ExceptionHandler(value = {AuthenticationException.class, JWTVerificationException.class})
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(
        AuthenticationException e, HttpServletRequest request
    ) {
        log.info("Authentication Exception: {}, Path: {}", e.getMessage(), request.getPathInfo());
        log.info("Token: {}", request.getHeader(AUTHORIZATION));
        return ErrorResponse.from(AUTH_MISSING_CREDENTIALS);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(
        AccessDeniedException e, HttpServletRequest request
    ) {
        log.info("Access Denied Exception: {}, Path: {}", e.getMessage(), request.getPathInfo());
        return ErrorResponse.from(AUTH_ACCESS_DENIED);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException e, HttpServletRequest request
    ) {
        log.info("Illegal Argument Exception: {}, Path: {}", e.getMessage(), request.getPathInfo());
        return ErrorResponse.from(ILLEGAL_INPUT_ARG);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(
        Exception e, HttpServletRequest request
    ) {
        log.info("Exception: {}, Path: {}", e.getMessage(), request.getPathInfo());
        return ErrorResponse.from(INTERNAL_SERVER_ERROR);
    }
}
