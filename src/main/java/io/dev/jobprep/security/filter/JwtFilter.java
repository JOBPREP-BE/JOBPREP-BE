package io.dev.jobprep.security.filter;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.dev.jobprep.security.jwt.JwtService;
import io.dev.jobprep.security.jwt.dto.TokenInfo;
import io.dev.jobprep.security.oauth.PrincipalDetails;
import io.dev.jobprep.security.oauth.application.PrincipalDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtFilter implements Filter {
    private final JwtService jwtService;
    private final PrincipalDetailsService principalDetailsService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String accessToken = request.getHeader("AccessToken");
        String refreshToken = request.getHeader("RefreshToken");

        //액세스 토큰 존재하면
        if(accessToken != null && !accessToken.isEmpty()) {
            if(jwtService.isTokenValid(accessToken)){
                filterChain.doFilter(request, response);
            }
            return;
        }
        if (refreshToken != null && !refreshToken.isEmpty()) {
            if(jwtService.isTokenValid(refreshToken)){
                String userId = jwtService.extractUserId(refreshToken);

                //새로운 리프레쉬, 액세스 토큰 만들기 위해 principalDetails 가져옴.
                PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
                String userEmail = principalDetails.getUsername();
                String userRoles = principalDetails
                        .getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

                //새로운 토큰 페어 생성
                TokenInfo tokenInfo= jwtService.generateTokenInfo(userId, userEmail, userRoles);

                Cookie accessTokenCookie = new Cookie("accessToken", tokenInfo.getAccessToken());
                accessTokenCookie.setHttpOnly(true);
                accessTokenCookie.setSecure(true);
                accessTokenCookie.setPath("/");

                Cookie refreshTokenCookie = new Cookie("refreshToken", tokenInfo.getRefreshToken());
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setSecure(true);
                refreshTokenCookie.setPath("/");

                response.addCookie(accessTokenCookie);
                response.addCookie(refreshTokenCookie);

                response.setStatus(HttpServletResponse.SC_OK); // HTTP 200 응답
                return;
            }
            return;
        }

        throw new JWTVerificationException("NO TOKEN FOUND");
    }
}
