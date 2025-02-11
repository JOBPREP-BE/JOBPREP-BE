package io.dev.jobprep.domain.security.oauth.domain;

import io.dev.jobprep.domain.users.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@ToString
@SuppressWarnings("unchecked")
public class OAuthAttributes {

    private static final String KAKAO = "kakao";
    private static final String GOOGLE = "google";
    private static final String NAVER = "naver";
    private static final String NAME = "name";
    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email";

    private final Map<String, Object> attributes;     // OAuth2 반환하는 유저 정보
    private final String name;
    private final String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String name, String email) {
        this.attributes = attributes;
        this.name = name;
        this.email = email;
    }

    public User toEntity () {
        return User.builder()
                .username(name)
                .email(email)
                .build();
    }

    public static OAuthAttributes of(String socialName, Map<String, Object> attributes) {
        if (KAKAO.equals(socialName)) {
            return ofKakao(attributes);
        } else if (GOOGLE.equals(socialName)) {
            return ofGoogle(attributes);
        } else if (NAVER.equals(socialName)) {
            return ofNaver(attributes);
        }
        return null;
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        List<String> info = fetchInfoFromAttributes(GOOGLE, attributes);
        return toEntity(info, attributes);
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        List<String> info = fetchInfoFromAttributes(KAKAO, attributes);
        return toEntity(info, attributes);
    }

    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        List<String> info = fetchInfoFromAttributes(NAVER, attributes);
        return toEntity(info, attributes);
    }

    private static List<String> fetchInfoFromAttributes(String social, Map<String, Object> attributes) {
        if (KAKAO.equals(social)) {
            Map<String, Object> account = fetchAttribute(attributes, "kakao_account");
            Map<String, Object> profile = fetchAttribute(attributes, "profile");
            return Arrays.asList(
                    String.valueOf(profile.get(NICKNAME)),
                    String.valueOf(account.get(EMAIL))
            );
        } else if (GOOGLE.equals(social)) {
            return Arrays.asList(
                    String.valueOf(attributes.get(NAME)),
                    String.valueOf(attributes.get(EMAIL))
            );
        } else if (NAVER.equals(social)) {
            Map<String, Object> attribute = fetchAttribute(attributes, "response");
            return Arrays.asList(
                    String.valueOf(attribute.get(NAME)),
                    String.valueOf(attribute.get(EMAIL))
            );
        } else {
            throw new IllegalArgumentException("Unsupported social type: " + social);
        }
    }

    private static OAuthAttributes toEntity(List<String> info, Map<String, Object> attributes) {
        if (isNull(attributes) || isNullOrBlank(info.get(0)) || isNullOrBlank(info.get(1))) {
            throw new IllegalArgumentException("Social attributes cannot be empty");
        }
        return OAuthAttributes.builder()
                .name(info.get(0))
                .email(info.get(1))
                .attributes(attributes)
                .build();
    }

    private static Map<String, Object> fetchAttribute(Map<String, Object> attributes, String key) {
        Map<String, Object> attribute = (Map<String, Object>) attributes.get(key);
        if (isNull(attribute)) {
            throw new IllegalArgumentException("Social attributes cannot be empty");
        }
        return attribute;
    }

    private static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    private static boolean isNull(Map<String, Object> attributes) {
        return attributes == null;
    }

}
