package io.dev.jobprep.domain.auth.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.dev.jobprep.core.properties.JwtProperties;
import io.dev.jobprep.domain.auth.jwt.dao.PrincipalDetails;
import io.dev.jobprep.domain.auth.application.PrincipalDetailsService;
import io.dev.jobprep.domain.auth.jwt.helper.JwtHelper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String IDENTIFICATION_TYPE = "Bearer ";
    private static final String REFRESH_HEADER = "x-refresh-token";
    private static final String SEPERATOR = " ";

    private final String header;
    private final JwtHelper jwtHelper;
    private final PrincipalDetailsService principalDetailsService;

    public JwtAuthenticationFilter(
        JwtProperties jwtProperties,
        JwtHelper jwtHelper,
        PrincipalDetailsService principalDetailsService
    ) {
        this.header = jwtProperties.getHeader();
        this.jwtHelper = jwtHelper;
        this.principalDetailsService = principalDetailsService;
    }

    @Override
    public void doFilterInternal(
        HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws IOException, ServletException {

        try {
            String accessToken = resolveAccessToken(request);
            log.info("accessToken: {}", accessToken);
            if (accessToken == null || !accessToken.contains(IDENTIFICATION_TYPE)) {
                request.setAttribute("exception", new JWTVerificationException("Invalid Token Type!"));
            } else {
                jwtHelper.verify(accessToken);
                setAuthentication(accessToken);
            }
        } catch (JWTVerificationException e) {
            request.setAttribute("exception", e);
        } catch (Exception e) {
            log.warn(e.getMessage());
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveAccessToken(HttpServletRequest request){
        String accessToken = request.getHeader(header);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(IDENTIFICATION_TYPE)) {
            return accessToken.split(SEPERATOR)[1].trim();
        }
        return null;
    }

    private void setAuthentication(String accessToken) {
        try {
            var principalDetails = getPrincipalDetailsFromToken(accessToken);
            var authentication = of(principalDetails, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new JWTVerificationException("Authentication failed");
        }
    }

    // 토큰에서 principalDetails 추출하는 메소드
    private PrincipalDetails getPrincipalDetailsFromToken(String accessToken){
        try {
            var decodedJWT = jwtHelper.verifyToken(accessToken);
            String userId = decodedJWT.getSubject();
            // TODO: clamins에서 가져올 때 roles은 'auth'가 아니라 'role'로 가져오도록 수정해놓음! 확인 바람!
            String role = decodedJWT.getClaims().get(JwtHelper.ROLES_STR).toString();

            return (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        } catch (Exception e) {
            throw new JWTVerificationException("Failed to get user details");
        }
    }

    private UsernamePasswordAuthenticationToken of(
        Object principal, Collection<? extends GrantedAuthority> authorities
    ) {
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESH_HEADER);
    }
}
