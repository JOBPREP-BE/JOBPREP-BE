package io.dev.jobprep.domain.security.jwt.application.provider;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.dev.jobprep.domain.security.jwt.application.dto.JwtToken;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.JWT;

import io.dev.jobprep.domain.security.jwt.exception.TokenStorageException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

import static io.dev.jobprep.exception.code.ErrorCode401.AUTH_MISSING_CREDENTIALS;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String TYPE = "Bearer";
    private static final String USER_ROLE = "auth";
    private static final String USER_EMAIL = "email";
    private static final String MASKER = "*****";
    private static final Long MONTH = 2_592_000_000L;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private Long accessTokenValidityTime;

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidityTime;

    @Value("${cookie.domain}")
    private String cookieDomain;

    public JwtToken sign(String userId, String userEmail, String userRoles) {

        String accessToken = signAccessToken(userId, userEmail, userRoles);
        log.info("Signed accessToken '{}'", masking(accessToken));

        String refreshToken = signRefreshToken(userId);
        log.info("Signed refreshToken '(CENSORED)' issued for user: {}", userId);

        return JwtToken.of(TYPE, accessToken, refreshToken);
    }

    public JwtToken signForFP(String userId, String userEmail, String userRoles) {

        String accessToken = signAccessTokenForFP(userId, userEmail, userRoles);
        log.info("Signed accessToken for FP '{}'", masking(accessToken));

        return JwtToken.of(TYPE, accessToken, null);
    }

    private String signAccessToken(String userId, String userEmail, String userRoles) {
        return JWT.create()
                .withSubject(userId)
                .withClaim(USER_ROLE, userRoles)
                .withClaim(USER_EMAIL, userEmail)
                .withIssuedAt(getCurrentDate())
                .withExpiresAt(calculateExpiryDate(accessTokenValidityTime))
                .sign(getAlgorithm());
    }

    private String signAccessTokenForFP(String userId, String userEmail, String userRoles) {
        return JWT.create()
                .withSubject(userId)
                .withClaim(USER_ROLE, userRoles)
                .withClaim(USER_EMAIL, userEmail)
                .withIssuedAt(getCurrentDate())
                .withExpiresAt(calculateExpiryDate(MONTH))
                .sign(getAlgorithm());
    }

    private String signRefreshToken(String userId) {
        try {
            return JWT.create()
                    .withSubject(userId)
                    .withExpiresAt(calculateExpiryDate(refreshTokenValidityTime))
                    .sign(getAlgorithm());
        } catch (Exception e) {
            log.error("Failed to sign token cause {}", e.getMessage());
            throw new JWTVerificationException("Failed to sign tokens");
        }
    }

    public Long verifyAndFetch(String token) {
        try {
            DecodedJWT decodedJWT = verify(token);
            return Long.parseLong(extractUserId(decodedJWT));
        } catch (Exception e) {
            log.warn("Failed to verity and fetch userId from token: {}", e.getMessage());
            throw new TokenStorageException(AUTH_MISSING_CREDENTIALS);
        }
    }

    public Long verifyWithoutExpiryAndFetch(String token) {
        try {
            DecodedJWT decodedJWT = verifyWithoutExpiry(token);
            String userId = extractUserId(decodedJWT);
            return Long.parseLong(userId);
        } catch (Exception e) {
            log.warn("Failed to verity and fetch userId for reissue from token: {}", e.getMessage());
            throw new TokenStorageException(AUTH_MISSING_CREDENTIALS);
        }
    }

    private DecodedJWT verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            return verifier.verify(token);
        } catch (Exception e) {
            throw new JWTVerificationException("Failed to verify token");
        }
    }

    private DecodedJWT verifyWithoutExpiry(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm())
                    .acceptExpiresAt(refreshTokenValidityTime)
                    .build();
            return verifier.verify(token);
        } catch (Exception e) {
            throw new JWTVerificationException("Failed to verify token");
        }
    }

    public String extractUserId(DecodedJWT decodedJWT) {
        try {
            return decodedJWT.getSubject();
        } catch (Exception e) {
            log.error("Failed to extract userId from token: {}", e.getMessage());
            throw new JWTVerificationException("Failed to extract user ID");
        }
    }

    public Cookie bake(String key, String value, Long tokenExpiry) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain(cookieDomain);
        cookie.setPath("/");
        cookie.setMaxAge(convert(tokenExpiry));
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }

    private String masking(String token) {
        return token.substring(0, 7).concat(MASKER);
    }

    private Date calculateExpiryDate(Long validityTime) {
        return getCurrentDate(System.currentTimeMillis() + validityTime);
    }

    private Date getCurrentDate(Long time){
        return new Date(time);
    }

    private Date getCurrentDate(){
        return new Date();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    private int convert(Long tokenExpiry) {
        return (int) (tokenExpiry / 1000);
    }

}