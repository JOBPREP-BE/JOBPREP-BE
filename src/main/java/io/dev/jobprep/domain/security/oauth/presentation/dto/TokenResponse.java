package io.dev.jobprep.domain.security.oauth.presentation.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
    private String Authorization;

    public TokenResponse(String accessToken) {
        this.Authorization = accessToken;
    }
}