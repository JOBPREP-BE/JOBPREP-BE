package io.dev.jobprep.domain.security.oauth.application;


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

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final String QUERY_PARAMETER = "?";
    private static final String TOKEN_PARAMETER = "otp_token=";

    private final OAuthRedisService oauthRedisService;

    @Value("${spring.security.oauth2.frontend-redirect.url}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        OAuthUserInfo userInfo = OAuthUserInfo.of(
                principalDetails.getUsername(),
                principalDetails.getEmail(),
                principalDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","))
        );

        String otpToken = generateToken();
        oauthRedisService.cache(otpToken, userInfo, Duration.ofMinutes(5));

        response.sendRedirect(generateRedirectUrl(otpToken));
    }

    private String generateRedirectUrl(String otpToken) {
        return redirectUrl + QUERY_PARAMETER + TOKEN_PARAMETER + otpToken;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
