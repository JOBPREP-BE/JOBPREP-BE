package io.dev.jobprep.domain.security.oauth.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthUserInfo {
    private String userId;
    private String email;
    private String authorities;
}