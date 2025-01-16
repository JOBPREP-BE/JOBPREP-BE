package io.dev.jobprep.domain.security.jwt.application;

import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.jwt.exception.TokenStorageException;
import io.dev.jobprep.exception.code.ErrorCode400;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtRedisService {
    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    private static final String SEPARATOR = ":";
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidityTime;

    public void saveRefreshToken(String userId, TokenInfo tokenInfo) /*throws TokenStorageException*/ {
        String key = getKey(userId);
        try {
            // 새 토큰 저장
            redisTemplate.opsForValue().set(
                    key,
                    tokenInfo.getRefreshToken(),
                    refreshTokenValidityTime,
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            log.error("Failed to rotate refresh token for user {}: {}", userId, e.getMessage());
            throw new TokenStorageException(ErrorCode400.RFT_CACHE_FALIURE);
        }
    }


    public void validateRefreshToken(String userId, String refreshToken) {
        String key = getKey(userId);
        try {
            String savedToken = redisTemplate.opsForValue().get(key);

            if (savedToken == null) {
                log.warn("No refresh token found for user: {}", userId);
                throw new TokenStorageException(ErrorCode400.RFT_VALIDATION_FAILURE);
            }

        } catch (Exception e) {
            throw new TokenStorageException(ErrorCode400.RFT_VALIDATION_FAILURE);
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
            log.error("Error occurred while deleting refresh token: {}", e.getMessage());
        }
    }

    // Redis Key 생성
    private String getKey(String userId) {
        return REFRESH_TOKEN_PREFIX + userId;
    }
}