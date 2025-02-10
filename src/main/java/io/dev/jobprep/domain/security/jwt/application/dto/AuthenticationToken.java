package io.dev.jobprep.domain.security.jwt.application.dto;

import lombok.Data;
import org.springframework.security.web.csrf.CsrfToken;

@Data
public class AuthenticationToken {

    private final JwtToken jwtToken;
    private final CsrfToken csrfToken;

    private AuthenticationToken(final JwtToken jwtToken, final CsrfToken csrfToken) {
        this.jwtToken = jwtToken;
        this.csrfToken = csrfToken;
    }

    public static AuthenticationToken of(final JwtToken jwtToken, final CsrfToken csrfToken) {
        return new AuthenticationToken(jwtToken, csrfToken);
    }
}
