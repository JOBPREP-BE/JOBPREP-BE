package io.dev.jobprep.domain.security.jwt.application;

import io.dev.jobprep.common.constants.TokenHeaderConstants;
import io.dev.jobprep.domain.security.oauth.application.OAuthCacheService;
import io.dev.jobprep.domain.security.oauth.application.PrincipalDetailsService;
import io.dev.jobprep.domain.security.oauth.application.dto.OAuthUserInfo;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.jwt.application.dto.JwtToken;
import io.dev.jobprep.domain.security.oauth.presentation.dto.res.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthIntegrationManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCacheService jwtCacheService;
    private final OAuthCacheService oauthCacheService;
    private final PrincipalDetailsService principalDetailsService;

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidity;

    public TokenResponse issueToken(final HttpServletRequest request, final HttpServletResponse response, String otpToken) {
        OAuthUserInfo userInfo = oauthCacheService.fetch(otpToken).orElse(null);
        if (userInfo == null) {
            throw new IllegalStateException("Invalid OTP token");
        }
        JwtToken token = issue(userInfo.getUserId());
        bakeCookie(token, response);
        return addWithCSRF(token, request);
    }

    public TokenResponse reissueToken(final HttpServletRequest request, final HttpServletResponse response, String refreshToken) {
        JwtToken token = reissue(refreshToken);
        bakeCookie(token, response);
        return addWithCSRF(token, request);
    }

    public void logout(final HttpServletResponse response, PrincipalDetails principalDetails) {
        deleteFromCache(principalDetails);
        bakeCookie(JwtToken.initTokenInfo(), response);
    }

    private JwtToken issue(String userId) {
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        JwtToken token = jwtTokenProvider.sign(userId, principalDetails.getEmail(), principalDetails.getUserRoles());
        jwtCacheService.cache(userId, token);
        return token;
    }

    private JwtToken reissue(String refreshToken) {
        String userId = verify(refreshToken);
        JwtToken token = issue(userId);
        log.info("Refresh token rotated for user '{}'", userId);
        return token;
    }

    private void deleteFromCache(PrincipalDetails principalDetails){
        SecurityContextHolder.clearContext();
        String userId = principalDetails.getUsername();
        jwtCacheService.evictCachedToken(userId);
    }

    private void bakeCookie(JwtToken jwtToken, HttpServletResponse response){
        Long rtExpiry = calculateExpiryTime(jwtToken.getRefreshToken());
        Cookie rtCookie = jwtTokenProvider.bake(TokenHeaderConstants.REFRESH_HEADER, jwtToken.getRefreshToken(), rtExpiry);
        response.addCookie(rtCookie);
    }

    private TokenResponse addWithCSRF(JwtToken jwtToken, HttpServletRequest request){
        CsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        CsrfToken csrfToken = tokenRepository.generateToken(request);
        return TokenResponse.of(jwtToken.getAccessToken(), csrfToken.getToken());
    }

    private String verify(String refreshToken) {
        String userId = String.valueOf(jwtTokenProvider.verifyAndFetch(refreshToken));
        jwtCacheService.verifyCachedToken(userId, refreshToken);
        return userId;
    }

    private Long calculateExpiryTime(String token) {
        return StringUtils.hasText(token) ? refreshTokenValidity : 0L;
    }

}
