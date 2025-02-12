package io.dev.jobprep.domain.security.oauth.presentation.dto.res;

import io.dev.jobprep.domain.security.jwt.application.dto.AuthenticationToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TokenResponse {

    @Schema(description = "액세스 토큰", example ="eyJhbGc******", implementation = String.class)
    private final String authorization;

    @Schema(description = "CSRF 토큰", example ="123e4567-e89b-12d3-a456-************")
    private final String csrfToken;

    private TokenResponse(String accessToken, String csrfToken) {
        this.authorization = accessToken;
        this.csrfToken = csrfToken;
    }

    public static TokenResponse of(AuthenticationToken token) {
        return new TokenResponse(token.getJwtToken().getAccessToken(), resolveCsrfToken(token));
    }

    private static String resolveCsrfToken(AuthenticationToken token) {
        return token.getCsrfToken() == null ? null : token.getCsrfToken().getToken();
    }
}