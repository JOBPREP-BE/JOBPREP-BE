package io.dev.jobprep.domain.security.oauth.application;

import io.dev.jobprep.domain.security.jwt.application.JwtRedisService;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.jwt.application.JwtService;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.util.TokenHeaderConstants;
import jakarta.servlet.http.Cookie;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final JwtRedisService jwtRedisService;
    @Value("${spring.security.oauth2.frontend-redirect.url}") // application.yml에 설정한 리다이렉트 URL
    private String redirectUrl;

    @Value("${jwt.access-token-validity}")
    private Long accessTokenValidity;

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidity;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userId = principalDetails.getUsername();
        String userEmail = principalDetails.getEmail();
        String userAuthority = principalDetails
                .getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        TokenInfo tokenInfo = jwtService.generateTokenInfo(userId, userEmail, userAuthority);

        jwtRedisService.saveRefreshToken(userId, tokenInfo);

        Cookie accessTokenCookie = jwtService.bake(TokenHeaderConstants.AUTHENTICATION_HEADER, tokenInfo.getAccessToken(), accessTokenValidity);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = jwtService.bake(TokenHeaderConstants.REFRESH_HEADER, tokenInfo.getRefreshToken(), refreshTokenValidity);
        response.addCookie(refreshTokenCookie);

        // 프론트엔드로 리다이렉트
        response.sendRedirect(redirectUrl);
    }

}
