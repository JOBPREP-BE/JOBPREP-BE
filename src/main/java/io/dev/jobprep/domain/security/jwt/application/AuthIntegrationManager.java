package io.dev.jobprep.domain.security.jwt.application;

import io.dev.jobprep.common.constants.TokenHeaderConstants;
import io.dev.jobprep.domain.security.jwt.application.dto.AuthenticationToken;
import io.dev.jobprep.domain.security.jwt.application.handler.TokenCacheAggregator;
import io.dev.jobprep.domain.security.jwt.application.provider.CsrfTokenProvider;
import io.dev.jobprep.domain.security.jwt.application.provider.JwtTokenProvider;
import io.dev.jobprep.domain.security.oauth.application.OAuthCacheService;
import io.dev.jobprep.domain.security.oauth.application.PrincipalDetailsService;
import io.dev.jobprep.domain.security.oauth.application.dto.OAuthUserInfo;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.jwt.application.dto.JwtToken;
import io.dev.jobprep.domain.security.oauth.presentation.dto.res.TokenResponse;
import io.dev.jobprep.domain.users.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthIntegrationManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final CsrfTokenProvider csrfTokenProvider;
    private final TokenCacheAggregator tokenCacheAggregator;
    private final OAuthCacheService oauthCacheService;
    private final PrincipalDetailsService principalDetailsService;

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidity;

    public TokenResponse issueToken(final HttpServletResponse response, String otpToken) {
        OAuthUserInfo userInfo = oauthCacheService.fetch(otpToken).orElse(null);
        if (userInfo == null) {
            throw new IllegalStateException("Invalid OTP token");
        }
        AuthenticationToken token = issue(userInfo.getUserId());
        bakeCookie(token.getJwtToken(), response);
        return TokenResponse.of(token);
    }

    public TokenResponse issueTokenViaNonOAuth(User developer) {
        AuthenticationToken token = issueForFP(String.valueOf(developer.getId()));
        return TokenResponse.of(token);
    }

    public TokenResponse reissueToken(final HttpServletResponse response, String refreshToken, String csrfToken) {
        AuthenticationToken token = reissue(refreshToken, csrfToken);
        bakeCookie(token.getJwtToken(), response);
        return TokenResponse.of(token);
    }

    public void logout(final HttpServletResponse response, PrincipalDetails principalDetails) {
        invalidateToken(principalDetails);
        bakeCookie(JwtToken.initTokenInfo(), response);
    }

    private AuthenticationToken issue(String userId) {
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        JwtToken jwtToken = jwtTokenProvider.sign(userId, principalDetails.getEmail(), principalDetails.getUserRoles());
        CsrfToken csrfToken = csrfTokenProvider.sign(userId);
        tokenCacheAggregator.cache(userId, jwtToken, csrfToken);
        return AuthenticationToken.of(jwtToken, csrfToken);
    }

    private AuthenticationToken issueForFP(String userId) {
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        JwtToken jwtToken = jwtTokenProvider.signForFP(userId, principalDetails.getEmail(), principalDetails.getUserRoles());
        return AuthenticationToken.of(jwtToken, null);
    }

    private AuthenticationToken reissue(String refreshToken, String csrfToken) {
        String userId = verify(refreshToken, csrfToken);
        AuthenticationToken token = issue(userId);
        log.info("Refresh token rotated for user '{}'", userId);
        return token;
    }

    private void invalidateToken(PrincipalDetails principalDetails){
        SecurityContextHolder.clearContext();
        tokenCacheAggregator.evictCache(principalDetails.getUsername());
    }

    private void bakeCookie(JwtToken jwtToken, HttpServletResponse response){
        Long rtExpiry = calculateExpiryTime(jwtToken.getRefreshToken());
        Cookie rtCookie = jwtTokenProvider.bake(TokenHeaderConstants.REFRESH_HEADER, jwtToken.getRefreshToken(), rtExpiry);
        response.addCookie(rtCookie);
    }

    private String verify(String refreshToken, String csrfToken) {
        String userId = String.valueOf(jwtTokenProvider.verifyWithoutExpiryAndFetch(refreshToken));
        tokenCacheAggregator.verifyCache(userId, refreshToken, csrfToken);
        return userId;
    }

    private Long calculateExpiryTime(String token) {
        return StringUtils.hasText(token) ? refreshTokenValidity : 0L;
    }

}
