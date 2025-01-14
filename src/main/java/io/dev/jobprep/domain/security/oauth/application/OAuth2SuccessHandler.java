package io.dev.jobprep.domain.security.oauth.application;

import io.dev.jobprep.domain.security.jwt.application.JwtRedisService;
import io.dev.jobprep.domain.security.jwt.application.JwtService;
import io.dev.jobprep.domain.security.oauth.application.dto.OAuthUserInfo;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final JwtRedisService jwtRedisService;
    private final OAuthRedisService oAuthRedisService;
    @Value("${spring.security.oauth2.frontend-redirect.url}") // application.yml에 설정한 리다이렉트 URL
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        String temporaryToken = UUID.randomUUID().toString();

        OAuthUserInfo userInfo = OAuthUserInfo.builder()
                .userId(principalDetails.getUsername())
                .email(principalDetails.getEmail())
                .authorities(principalDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .build();

        oAuthRedisService.saveTemporaryToken(temporaryToken, userInfo, Duration.ofMinutes(5));

        String redirectUrlWithToken = redirectUrl + "?temp_token=" + temporaryToken;

        response.sendRedirect(redirectUrlWithToken);
    }
