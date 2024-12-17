package io.dev.jobprep.security.oauth;

import io.dev.jobprep.security.jwt.JwtService;
import io.dev.jobprep.security.jwt.dto.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userId = principalDetails.getUsername().toString();
        String userEmail = principalDetails.getEmail();
        String userAuthority = principalDetails
                .getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        TokenInfo tokenInfo = jwtService.generateTokenInfo(userId, userEmail, userAuthority);

        // 쿠키 설정 - HttpOnly, Secure 설정 권장 (HTTPS 환경 필수)
        response.setHeader("Authorization", tokenInfo.getGrantType() + " " + tokenInfo.getAccessToken());
        response.setHeader("X-Refresh-Token", tokenInfo.getRefreshToken());

        response.sendRedirect("/api/v1/applicationstatus/?userId="+userId);
    }
}
