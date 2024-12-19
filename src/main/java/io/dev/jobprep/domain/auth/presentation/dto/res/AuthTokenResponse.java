package io.dev.jobprep.domain.auth.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Access Token 재발급 응답")
@Getter
public class AuthTokenResponse {

    @Schema(description = "Access Token")
    private final String accessToken;

    @Schema(description = "Refresh Token")
    private final String refreshToken;

    private AuthTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static AuthTokenResponse of(String accessToken, String refreshToken) {
        return new AuthTokenResponse(accessToken, refreshToken);
    }

}
