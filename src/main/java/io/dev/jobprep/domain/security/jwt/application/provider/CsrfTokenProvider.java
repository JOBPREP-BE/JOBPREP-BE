package io.dev.jobprep.domain.security.jwt.application.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsrfTokenProvider {

    private static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
    private static final String MASKER = "*****";

    public CsrfToken sign(String userId) {
        CsrfToken csrfToken = this.generateToken();
        log.info("Signed csrfToken '{}' issued for user: {}", masking(csrfToken.getToken()), userId);
        return csrfToken;
    }

    private CsrfToken generateToken() {
        String token = UUID.randomUUID().toString();
        return new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, DEFAULT_CSRF_PARAMETER_NAME, token);
    }

    private String masking(String token) {
        return token.substring(0, 7).concat(MASKER);
    }
}
