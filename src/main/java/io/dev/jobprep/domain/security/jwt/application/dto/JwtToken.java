package io.dev.jobprep.domain.security.jwt.application.dto;

import lombok.Data;

@Data
public class JwtToken {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;

    private JwtToken() {
        this.grantType = "";
        this.accessToken = "";
        this.refreshToken = "";
    }

    private JwtToken(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static JwtToken initTokenInfo() {
        return new JwtToken();
    }

    public static JwtToken of(String grantType, String accessToken, String refreshToken) {
        return new JwtToken(grantType, accessToken, refreshToken);
    }

}