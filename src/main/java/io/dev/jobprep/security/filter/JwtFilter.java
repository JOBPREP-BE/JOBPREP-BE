package io.dev.jobprep.security.filter;


import com.auth0.jwt.exceptions.JWTVerificationException;
import io.dev.jobprep.security.oauth.application.JwtService;
import io.dev.jobprep.security.jwt.dto.TokenInfo;
import io.dev.jobprep.security.oauth.PrincipalDetails;
import io.dev.jobprep.security.oauth.application.PrincipalDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final PrincipalDetailsService principalDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_HEADER = "x-refresh-token";

    private static final List<String> PERMIT_ALL_PATHS = Arrays.asList(
            "/login",
            "/api/v1/oauth2/login-urls",
            "/api/v1/oauth2/authorize",
            "/oauth2/authorization",
            "/login/oauth2/code/"
    );
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 인증이 필요없는 경로인 경우 필터를 적용하지 않음
        return PERMIT_ALL_PATHS.stream()
                .anyMatch(permitPath -> path.startsWith(permitPath));
    }
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String accessToken = resolveAccessToken(request);
            //액세스 토큰 존재하면
            if (StringUtils.hasText(accessToken)) {
                jwtService.isTokenValid(accessToken);//토큰 valid
                setAuthentication(accessToken);
            } else{
                request.setAttribute( "exception", new JWTVerificationException("NO TOKEN FOUND"));
            }
        }catch (JWTVerificationException e) {
            request.setAttribute("exception", e);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    // 토큰에서 principalDetails 추출하는 메소드
    private PrincipalDetails getPrincipalDetailsFromToken(String Token){
        try {
            DecodedJWT decodeJWT = jwtService.verifyToken(Token);
            String userId = jwtService.extractUserId(decodeJWT);
            return (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        } catch (Exception e) {
            throw new JWTVerificationException("Failed to get user details");
        }
    }
    private void setAuthentication(String accessToken) {
        try {
            PrincipalDetails principalDetails = getPrincipalDetailsFromToken(accessToken);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principalDetails,
                            null,
                            principalDetails.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new JWTVerificationException("Authentication failed");
        }
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
