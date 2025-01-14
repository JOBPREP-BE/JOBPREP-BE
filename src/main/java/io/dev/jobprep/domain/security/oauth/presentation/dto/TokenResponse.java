package io.dev.jobprep.domain.security.oauth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TokenResponse {
   @Schema(description = "액세스 토큰", example ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIiwiYXV0aCI6Ik5PUk1BTCIs...", implementation = String.class)
    private String Authorization;

    public TokenResponse(String accessToken) {
        this.Authorization = accessToken;
    }
}