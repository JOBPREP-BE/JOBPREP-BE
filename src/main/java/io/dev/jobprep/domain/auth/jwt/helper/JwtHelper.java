package io.dev.jobprep.domain.auth.jwt.helper;

import static com.auth0.jwt.JWT.require;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.dev.jobprep.core.properties.JwtProperties;
import io.dev.jobprep.core.properties.KakaoProperties;
import io.dev.jobprep.domain.auth.jwt.dao.JwtToken;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtHelper {

    public static final String ROLES_STR = "roles";
    public static final String EMAIL_STR = "email";
    private static final String IDENTIFICATION_TYPE = "Bearer";
    private static final Long HOUR_TO_MILLIS = 3600000L;

    private final String issuer;
    private final long accessTokenExpirySeconds;
    private final long refreshTokenExpirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtHelper(JwtProperties jwtProperties, KakaoProperties kakaoProperties) {
        this.issuer = jwtProperties.getIssuer();
        this.accessTokenExpirySeconds = hoursToMillis(jwtProperties.getAccessTokenExpiryHour());
        this.refreshTokenExpirySeconds = hoursToMillis(jwtProperties.getRefreshTokenExpiryHour());
        // TODO: 소셜로그인 플랫폼별로 algorithm 생성을 위한 client-secret 어떻게 관리할지?
        this.algorithm = Algorithm.HMAC256(kakaoProperties.getClientSecret());
        this.jwtVerifier = require(algorithm).withIssuer(issuer).build();
    }

    public JwtToken sign(String userId, String userEmail, String userRole) {
        Date today = new Date();
        String accessToken = generateAccessToken(today, userId, userRole, userEmail);
        String refreshToken = generateRefreshToken(today, userId);
        return JwtToken.of(
            Long.parseLong(userId),
            IDENTIFICATION_TYPE,
            accessToken,
            refreshToken
        );
    }

    private String generateAccessToken(Date today, String userId, String userRole, String userEmail) {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(userId)
            .withClaim(ROLES_STR, userRole)
            .withClaim(EMAIL_STR, userEmail)
            .withIssuedAt(today)
            .withExpiresAt(calculateExpirySeconds(today, accessTokenExpirySeconds))
            .sign(algorithm);
    }

    private String generateRefreshToken(Date today, String userId) {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(userId)
            .withIssuedAt(today)
            .withExpiresAt(calculateExpirySeconds(today, refreshTokenExpirySeconds))
            .sign(algorithm);
    }

    public void verify(String token) {
        try {
            var decodedJWT = verifyToken(token);
            if (decodedJWT.getSubject() == null || !decodedJWT.getClaims().containsKey(ROLES_STR)
                || !decodedJWT.getClaims().containsKey(EMAIL_STR)) {
                throw new JWTVerificationException("Invalid token");
            }
        } catch (TokenExpiredException e) {
            throw e;
        } catch (Exception e) {
            // 다른 예외들은 JWTVerificationException으로 감싸기
            log.error("Unexpected error during token validation: {}", e.getMessage());
            throw new JWTVerificationException("Token validation failed");
        }

    }

    public DecodedJWT verifyToken(String token) {
        try {
            // 토큰의 유효성을 검증하면서, 토큰의 만료일자도 자동으로 확인함
            return jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new JWTVerificationException("Failed to verify token");
        }
    }

    public DecodedJWT verifyWithoutExpiry(String token) {
        JWTVerifier verifier = require(algorithm).acceptExpiresAt(refreshTokenExpirySeconds).build();
        return verifier.verify(token);
    }

    public DecodedJWT verifyRefreshToken(String token) {
        return jwtVerifier.verify(token);
    }

    private static long hoursToMillis(int hour) {
        return hour * HOUR_TO_MILLIS;
    }

    private static Date calculateExpirySeconds(Date today, long tokenExpirySeconds) {
        return new Date(today.getTime() + tokenExpirySeconds);
    }
}
