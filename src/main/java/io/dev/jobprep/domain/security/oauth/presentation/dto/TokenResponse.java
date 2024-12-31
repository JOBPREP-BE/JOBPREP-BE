package io.dev.jobprep.domain.security.oauth.presentation.dto;

import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String grantType;
    private String authorization;
    private String xRefreshToken;

    public static TokenResponse from(TokenInfo tokenInfo) {
        return TokenResponse.builder()
                .grantType(tokenInfo.getGrantType())
                .authorization(tokenInfo.getAccessToken())
                .xRefreshToken(tokenInfo.getRefreshToken())
                .build();
    }
}