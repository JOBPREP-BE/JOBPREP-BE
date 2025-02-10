package io.dev.jobprep.domain.security.jwt.application;

import io.dev.jobprep.domain.security.jwt.application.dto.JwtToken;
import io.dev.jobprep.domain.security.jwt.exception.TokenStorageException;
import io.dev.jobprep.exception.code.ErrorCode400;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtCacheService {

    private static final String REFRESH_TOKEN_PREFIX = "RT:";

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidityTime;

    public void cache(String userId, JwtToken jwtToken) {
        String key = generateKey(userId);
        try {
            redisTemplate.opsForValue().set(
                    key,
                    jwtToken.getRefreshToken(),
                    refreshTokenValidityTime,
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            log.error("Failed to rotate refresh token for user '{}': {}", userId, e.getMessage());
            throw new TokenStorageException(ErrorCode400.RFT_CACHE_FALIURE);
        }
    }

    public void verifyCachedToken(String userId, String refreshToken) {
        String key = generateKey(userId);
        try {
            String savedToken = redisTemplate.opsForValue().get(key);
            if (savedToken == null) {
                log.warn("No refresh token found for user '{}'", userId);
                throw new TokenStorageException(ErrorCode400.RFT_VALIDATION_FAILURE);
            }
            if (!savedToken.equals(refreshToken)) {
                log.warn("Refresh token does not match stored token '{}'", refreshToken);
                throw new TokenStorageException(ErrorCode400.RFT_VALIDATION_FAILURE);
            }
        } catch (Exception e) {
            throw new TokenStorageException(ErrorCode400.RFT_VALIDATION_FAILURE);
        }

    }

    public void evictCachedToken(String userId) {
        String key = generateKey(userId);
        try {
            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                log.info("Refresh token deleted for user '{}'", userId);
            } else {
                log.warn("No refresh token found to delete issued for user '{}'", userId);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting refresh token issued for user '{}': {}", userId, e.getMessage());
        }
    }

    private String generateKey(String userId) {
        return REFRESH_TOKEN_PREFIX + userId;
    }
}