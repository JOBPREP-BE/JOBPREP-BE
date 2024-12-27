package io.dev.jobprep.domain.security.oauth.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.jwt.application.JwtService;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.oauth.presentation.dto.TokenResponse;
import jakarta.servlet.ServletException;
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
    final JwtService jwtService;

    @Value("${spring.security.oauth2.frontend-redirect.url}") // application.yml에 설정한 리다이렉트 URL
    private String redirectUrl;

    @Value("${jwt.access-token-validity}")
    private Long accessTokenValidity;  // 밀리초 단위로 받아옴

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidity;  // 밀리초 단위로 받아옴

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
        TokenResponse tokenResponse = TokenResponse.from(tokenInfo);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(tokenResponse);

        Cookie accessTokenCookie = new Cookie("accessToken", tokenInfo.getGrantType() +' '+ tokenInfo.getAccessToken());
        accessTokenCookie.setHttpOnly(true);  // JavaScript에서 접근 불가
        accessTokenCookie.setSecure(true);    // HTTPS에서만 전송
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int)(accessTokenValidity/1000));    // 1시간

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenInfo.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int)(refreshTokenValidity/1000));  // 7일

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 프론트엔드로 리다이렉트
        response.sendRedirect(redirectUrl);

        // 성공 메시지 JSON 작성
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(jsonResponse);
    }
}
