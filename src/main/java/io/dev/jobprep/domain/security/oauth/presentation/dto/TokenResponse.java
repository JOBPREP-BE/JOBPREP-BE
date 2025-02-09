package io.dev.jobprep.domain.security.oauth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TokenResponse {

    @Schema(description = "액세스 토큰", example ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIiwiYXV0aCI6Ik5PUk1BTCIs...", implementation = String.class)
    private final String authorization;

    @Schema(description = "CSRF 토큰", example ="123e4567-e89b-12d3-a456-426614174000")
    private final String csrfToken;

    public TokenResponse(String accessToken, String csrfToken) {
        this.authorization = accessToken;
        this.csrfToken = csrfToken;
    }
}