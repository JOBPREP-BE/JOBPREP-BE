package io.dev.jobprep.security.jwt;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.dev.jobprep.security.jwt.dto.TokenInfo;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.JWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Date;

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
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidityTime))
                .sign(getAlgorithm());
    }

    public Boolean isTokenValid(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return !isTokenExpired(decodedJWT);
    }

    //토큰 만료 확인
    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        boolean expired = decodedJWT.getExpiresAt().before(new Date());
        if(expired) {
            Instant expirationTime = decodedJWT.getExpiresAt().toInstant();
            throw new TokenExpiredException("Token expired on ", expirationTime);
        }
        return expired;
    }

    // Extract username (subject) from token
    public String extractUserId(String token) {
        try {
            return verifyToken(token).getSubject();
        } catch (JWTDecodeException e) {
            log.info("Error decoding JWT: {}", e.getMessage());
            return null;
        }
    }

    //Exract UserRole from token
    public String extractUserEmail(String token) {
        try {
            return verifyToken(token).getClaim("email").asString();
        } catch (JWTDecodeException e) {
            log.info("Error decoding JWT: {}", e.getMessage());
            return null;
        }
    }

    public String extractUserRole (String token) {
        try {
            return verifyToken(token).getClaim("auth").toString();
        } catch (JWTDecodeException e) {
            log.info("Error decoding JWT: {}", e.getMessage());
            return null;
        }
    }

    // Verify and decode a JWT
    private DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }

}