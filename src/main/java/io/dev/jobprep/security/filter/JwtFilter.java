package io.dev.jobprep.security.filter;


import com.auth0.jwt.exceptions.JWTVerificationException;
import io.dev.jobprep.security.jwt.JwtService;
import io.dev.jobprep.security.jwt.dto.TokenInfo;
import io.dev.jobprep.security.oauth.PrincipalDetails;
import io.dev.jobprep.security.oauth.application.PrincipalDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final PrincipalDetailsService principalDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_HEADER = "x-refresh-token";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //로그인 관련 path이면 다음 필터로 이동
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/login")) {
            filterChain.doFilter(request, response); // 다음 필터로 이동
            return;
        }

        String accessToken = resolveAccessToken(request);
        //액세스 토큰 존재하면
        if(accessToken != null && !accessToken.isEmpty()) {
            try{
            if(jwtService.isTokenValid(accessToken)){//토큰 valid
                PrincipalDetails principalDetails =  getPrincipalDetailsFromToken(accessToken);
                setAuthentication(accessToken);
            }
            }catch(JWTVerificationException e){
                request.setAttribute("exception", e);
            }
            filterChain.doFilter(request, response);
            return;
        }

        //액세스 토큰이 없을 시
        String refreshToken = resolveRefreshToken(request);
        if (refreshToken != null && !refreshToken.isEmpty()) {
            try{
            if(jwtService.isTokenValid(refreshToken)){
                //새로운 리프레쉬, 액세스 토큰 만들기 위해 principalDetails 가져옴.
                PrincipalDetails principalDetails = getPrincipalDetailsFromToken(refreshToken);

                String userId = principalDetails.getUsername();
                String userEmail = principalDetails.getEmail();
                String userRoles = principalDetails
                        .getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

                //새로운 토큰 페어 생성
                TokenInfo tokenInfo= jwtService.generateTokenInfo(userId, userEmail, userRoles);
                // 쿠키 설정 - HttpOnly, Secure 설정 권장 (HTTPS 환경 필수)
                response.setHeader("Authorization", tokenInfo.getGrantType() + " " + tokenInfo.getAccessToken());
                response.setHeader("X-Refresh-Token", tokenInfo.getRefreshToken());

                response.setStatus(HttpServletResponse.SC_OK); // HTTP 200 응답
                return;
            }
            }catch(JWTVerificationException e){
                request.setAttribute("exception", e);
            }
            filterChain.doFilter(request, response);
            return;
        }
        request.setAttribute( "exception", new JWTVerificationException("NO TOKEN FOUND"));
    }

    // 토큰에서 principalDetails 추출하는 메소드
    private PrincipalDetails getPrincipalDetailsFromToken(String Token){
        String userId = jwtService.extractUserId(Token);
        return (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
    }
    private void setAuthentication(String accessToken) {
        PrincipalDetails principalDetails = getPrincipalDetailsFromToken(accessToken);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveAccessToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.split(" ")[1].trim();
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESH_HEADER);
    }
}
