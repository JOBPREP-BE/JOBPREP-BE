package io.dev.jobprep.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dev.jobprep.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.dev.jobprep.exception.code.ErrorCode403.AUTH_ACCESS_DENIED;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;  // 추가 필요
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        log.info("Access Denied Exception: {}, Path: {}",
                accessDeniedException.getMessage(),
                request.getRequestURI());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        ErrorResponse.from(AUTH_ACCESS_DENIED)
                )
        );
    }
}