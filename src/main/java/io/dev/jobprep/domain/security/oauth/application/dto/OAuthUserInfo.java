package io.dev.jobprep.domain.security.oauth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthUserInfo {
    private String userId;
    private String email;
    private String authorities;
}