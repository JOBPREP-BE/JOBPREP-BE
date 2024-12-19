package io.dev.jobprep.security.oauth;

import io.dev.jobprep.domain.users.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@ToString
@Slf4j
public class OAuthAttributes {
    private final Map<String, Object> attributes;     // OAuth2 반환하는 유저 정보
    private final String name;
    private final String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String name, String email) {
        this.attributes = attributes;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributes of(String socialName, Map<String, Object> attributes) {
        if ("kakao".equals(socialName)) {
            return ofKakao(attributes);
        } else if ("google".equals(socialName)) {
            return ofGoogle(attributes);
        } else if ("naver".equals(socialName)) {
            return ofNaver(attributes);
        }
        return null;
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        String name = String.valueOf(attributes.get("name"));
        String email = String.valueOf(attributes.get("email"));

        return OAuthAttributes.builder()
                .name(name)
                .email(email)
                .attributes(attributes)
                .build();
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        String name = String.valueOf(kakaoProfile.get("nickname"));
        String email = String.valueOf(kakaoAccount.get("email"));

        return OAuthAttributes.builder()
                .name(name)
                .email(email)
                .attributes(attributes)
                .build();
    }

    public static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        String name = String.valueOf(response.get("name"));
        String email = String.valueOf(response.get("email"));

        return OAuthAttributes.builder()
                .name(name)
                .email(email)
                .attributes(attributes)
                .build();
    }

    public User toEntity () {
        return User.builder()
                .username(name)
                .email(email)
                .build();
    }
}
