package io.dev.jobprep.domain.security.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.jwt.application.provider.JwtTokenProvider;
import io.dev.jobprep.domain.security.oauth.application.PrincipalDetailsService;
import io.dev.jobprep.common.constants.TokenHeaderConstants;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipalDetailsService principalDetailsService;

    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/actuator",
            "/api/v1/oauth2/reissue",
            "/oauth/callback"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("Incoming request {}", request.getRequestURI());

        return EXCLUDE_PATHS.stream()
                .anyMatch(path -> {
                    log.info("Matched result: {}", request.getRequestURI().startsWith(path));
                    return request.getRequestURI().startsWith(path);
                }
        );
    }

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {

        try {
            String accessToken = resolveAccessToken(request);
            if (StringUtils.hasText(accessToken)) {
                PrincipalDetails principalDetails = verifyAndFetch(accessToken);
                if (principalDetails != null) {
                    log.debug("Authentication successful for user: {}", principalDetails.getUsername());
                    setAuthentication(principalDetails);
                } else {
                    log.warn("Failed to get principal details from token");
                    SecurityContextHolder.clearContext();
                    request.setAttribute("exception", new JWTVerificationException("No user-details found"));
                }
            } else {
                log.debug("No token found in request headers");
                SecurityContextHolder.clearContext();
                request.setAttribute( "exception", new JWTVerificationException("No token found"));
            }
        } catch (JWTVerificationException e) {
            log.warn("JWT verification failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            request.setAttribute("exception", e);
        } catch (Exception e) {
            log.error("Unexpected error during authentication: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            request.setAttribute("exception", e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private PrincipalDetails verifyAndFetch(String token){
        try {
            String userId = String.valueOf(jwtTokenProvider.verifyAndFetch(token));
            return (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        } catch (Exception e) {
            throw new JWTVerificationException("Failed to get user-details");
        }
    }

    private void setAuthentication(PrincipalDetails principalDetails) {
        try {
            if (principalDetails == null) {
                throw new JWTVerificationException("No user-details found");
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
        String bearerToken = request.getHeader(TokenHeaderConstants.AUTHENTICATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TokenHeaderConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(TokenHeaderConstants.TOKEN_PREFIX.length()).trim();
        }
        return null;
    }
}
