package io.dev.jobprep.domain.security.jwt.application;

import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.jwt.exception.TokenStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtRedisService {
    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    private static final String SAPERATOR = ":";
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidityTime;

    public void saveRefreshToken(String userId, TokenInfo tokenInfo) /*throws TokenStorageException*/ {
        String key = getKey(userId);
        //TODO: Exception handling 하기
//        try {
            redisTemplate.opsForValue().set(
                    key,
                    tokenInfo.getRefreshToken(),
                    refreshTokenValidityTime,
                    TimeUnit.MILLISECONDS
            );
//        } catch (Exception e) {
//            throw new TokenStorageException("Failed to save refresh token to redis");
//        }
    }


    public boolean validateRefreshToken(String userId, String refreshToken) {
        String key = getKey(userId);
        String savedToken = redisTemplate.opsForValue().get(key);

        if (savedToken == null) {
            log.warn("No refresh token found for user: {}", userId);
            return false;
        }

        return savedToken.equals(refreshToken);
    }

    // Refresh Token 재발급 시 기존 토큰 삭제 및 새 토큰 저장
    public void rotateRefreshToken(String userId, TokenInfo newTokenInfo) {
        String key = getKey(userId);

        try {
            // 기존 토큰 삭제
            redisTemplate.delete(key);

            // 새 토큰 저장
            redisTemplate.opsForValue().set(
                    key,
                    newTokenInfo.getRefreshToken(),
                    refreshTokenValidityTime,
                    TimeUnit.MILLISECONDS
            );

            log.info("Refresh token rotated for user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to rotate refresh token for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to rotate refresh token");
        }
    }

    //로그아웃 시 Refresh Token 삭제
    public void deleteRefreshToken(String userId) {
        String key = getKey(userId);
        try {
            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                log.info("Refresh token deleted for user: {}", userId);
            } else {
                log.warn("No refresh token found to delete for user: {}", userId);
            }
        } catch (Exception e) {
            log.error("Failed to delete refresh token for user {}: {}", e.getMessage());
            throw new RuntimeException("Failed to delete refresh token");
        }
    }

    // Redis Key 생성
    private String getKey(String userId) {
        return REFRESH_TOKEN_PREFIX + userId;
    }
}