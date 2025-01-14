package io.dev.jobprep.domain.security.jwt.filter;


import com.auth0.jwt.exceptions.JWTVerificationException;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.jwt.application.JwtService;
import io.dev.jobprep.domain.security.oauth.application.PrincipalDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final PrincipalDetailsService principalDetailsService;

    private static final String AUTHORIZATION_HEADER = "authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_HEADER = "xRefreshToken";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String accessToken = resolveAccessToken(request);
            //액세스 토큰 존재하면
            if (StringUtils.hasText(accessToken)) {
                jwtService.isTokenValid(accessToken);//토큰 valid
                PrincipalDetails principalDetails = getPrincipalDetailsFromToken(accessToken);
                if (principalDetails != null) {
                    log.debug("Authentication successful for user: {}", principalDetails.getUsername());
                    setAuthentication(principalDetails);
                    filterChain.doFilter(request, response);
                } else{
                    log.warn("Failed to get principal details from token");
                    SecurityContextHolder.clearContext();
                    request.setAttribute("exception", new JWTVerificationException("User details not found"));
                    filterChain.doFilter(request, response);
                }
            } else{
                log.debug("No token found in request headers");
                SecurityContextHolder.clearContext();
                request.setAttribute( "exception", new JWTVerificationException("NO TOKEN FOUND"));
                filterChain.doFilter(request, response);
            }
        }catch (JWTVerificationException e) {
            log.warn("JWT verification failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            request.setAttribute("exception", e);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Unexpected error during authentication: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            request.setAttribute("exception", e);
            filterChain.doFilter(request, response);
        }
    }

    // 토큰에서 principalDetails 추출하는 메소드
    private PrincipalDetails getPrincipalDetailsFromToken(String Token){
        try {
            DecodedJWT decodeJWT = jwtService.verifyNDecodeToken(Token);
            String userId = jwtService.extractUserId(decodeJWT);
            return (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        } catch (Exception e) {
            throw new JWTVerificationException("Failed to get user details");
        }
    }
    private void setAuthentication(PrincipalDetails principalDetails) {
        try {
            if (principalDetails == null) {
                throw new JWTVerificationException("User details not found");
            }
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
            return bearerToken.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESH_HEADER);
    }
}
