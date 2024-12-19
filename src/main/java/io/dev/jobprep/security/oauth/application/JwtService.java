package io.dev.jobprep.security.oauth.application;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.dev.jobprep.security.jwt.dto.TokenInfo;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.JWT;

import io.dev.jobprep.security.oauth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-validity}")
    private Long accessTokenValidityTime;
    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidityTime;
    //토큰 생성기
    //리프레쉬토큰, 액세스 토큰 재생성

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    public TokenInfo generateTokenInfo(String userId, String userEmail, String userRoles ){
        // Create Access Token
        String accessToken = generateAccessToken(userId, userEmail, userRoles);

        // Create Refresh Token
        String refreshToken = generateRefreshToken(userId);

        //JWT 토큰 반환
        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    private String generateAccessToken(String userId, String userEmail, String userRoles ) {
        return JWT.create()
                .withSubject(userId)
                .withClaim("auth", userRoles)
                .withClaim("email", userEmail)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidityTime))
                .sign(getAlgorithm());
    }

    private String generateRefreshToken(String userId) {
        try {
            return JWT.create()
                    .withSubject(userId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidityTime))
                    .sign(getAlgorithm());
        }catch (Exception e) {
                log.error("Failed to generate token info: {}", e.getMessage());
                throw new JWTVerificationException("Failed to generate tokens");
            }
    }

    public void isTokenValid(String token) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            isTokenExpired(decodedJWT);
        } catch (TokenExpiredException e) {
            throw e;
        } catch (Exception e) {
            // 다른 예외들은 JWTVerificationException으로 감싸기
            log.error("Unexpected error during token validation: {}", e.getMessage());
            throw new JWTVerificationException("Token validation failed");
        }
    }

    //토큰 만료 확인
    private void isTokenExpired(DecodedJWT decodedJWT) {
        boolean expired = decodedJWT.getExpiresAt().before(new Date());
        if(expired) {
            Instant expirationTime = decodedJWT.getExpiresAt().toInstant();
            throw new TokenExpiredException("Token expired on ", expirationTime);
        }
    }

    // Verify and decode a JWT
    public DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            return verifier.verify(token);
        } catch (Exception e) {
            throw new JWTVerificationException("Failed to verify token");
        }
    }

    // Extract username (subject) from token
    public String extractUserId(DecodedJWT decodedJWT) {
        try {
            return decodedJWT.getSubject();
        } catch (Exception e) {
            log.error("Failed to extract user ID from token: {}", e.getMessage());
            throw new JWTVerificationException("Failed to extract user ID");
        }
    }

    //Exract UserRole from token
    public String extractUserEmail(DecodedJWT decodedJWT) {
        try {
            return decodedJWT.getClaim("email").asString();
        } catch (Exception e) {
            log.error("Failed to extract email from token: {}", e.getMessage());
            throw new JWTVerificationException("Failed to extract email");
        }
    }

    public String extractUserRole (DecodedJWT decodedJWT) {
        try {
            return decodedJWT.getClaim("auth").toString();
        } catch (Exception e) {
            log.error("Failed to extract user role from token: {}", e.getMessage());
            throw new JWTVerificationException("Failed to extract user role");
        }
    }
}