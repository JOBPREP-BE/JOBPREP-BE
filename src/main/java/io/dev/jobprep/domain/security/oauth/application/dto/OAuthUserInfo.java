package io.dev.jobprep.domain.security.oauth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthUserInfo {

    private final String userId;
    private final String email;
    private final String authorities;

    @Builder
    private OAuthUserInfo(String userId, String email, String authorities) {
        this.userId = userId;
        this.email = email;
        this.authorities = authorities;
    }

    public static OAuthUserInfo of(String userId, String email, String authorities) {
        return new OAuthUserInfo(userId, email, authorities);
    }

}