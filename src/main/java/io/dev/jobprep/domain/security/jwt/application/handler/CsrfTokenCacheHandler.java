package io.dev.jobprep.domain.security.jwt.application.handler;

import io.dev.jobprep.domain.security.jwt.exception.TokenStorageException;
import io.dev.jobprep.exception.code.ErrorCode400;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CsrfTokenCacheHandler extends AbstractTokenCacheHandler {

    private static final String CSRF_TOKEN_PREFIX = "CSRF:";

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidityTime;

    protected CsrfTokenCacheHandler(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected void cache(String userId, String token) {
        String key = this.generateKey(userId);
        try {
            redisTemplate.opsForValue().set(
                    key,
                    token,
                    refreshTokenValidityTime,
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            log.error("Failed to rotate csrf token for user '{}': {}", userId, e.getMessage());
            throw new TokenStorageException(ErrorCode400.CSRF_CACHE_FAILURE);
        }
    }

    @Override
    protected void verifyCache(String userId, String token) {
        String key = this.generateKey(userId);
        try {
            String savedToken = redisTemplate.opsForValue().get(key);
            if (savedToken == null) {
                log.warn("No csrf token found for user '{}'", userId);
                throw new TokenStorageException(ErrorCode400.CSRF_VALIDATION_FAILURE);
            }
            if (!savedToken.equals(token)) {
                log.warn("CSRF token does not match stored token '{}'", token);
                throw new TokenStorageException(ErrorCode400.CSRF_VALIDATION_FAILURE);
            }
        } catch (Exception e) {
            throw new TokenStorageException(ErrorCode400.CSRF_VALIDATION_FAILURE);
        }
    }

    @Override
    protected void evictCache(String userId) {
        String key = this.generateKey(userId);
        try {
            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                log.info("CSRF token deleted for user '{}'", userId);
            } else {
                log.warn("No CSRF token found to delete issued for user '{}'", userId);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting csrf token issued for user '{}': {}", userId, e.getMessage());
        }
    }

    @Override
    protected String generateKey(String userId) {
        return CSRF_TOKEN_PREFIX + userId;
    }
}
