package io.dev.jobprep.domain.security.jwt.application;

import io.dev.jobprep.domain.security.jwt.application.dto.JwtToken;
import io.dev.jobprep.domain.security.jwt.exception.TokenStorageException;
import io.dev.jobprep.exception.code.ErrorCode400;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenCacheHandler {

    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    private static final String CSRF_TOKEN_PREFIX = "CSRF:";

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidityTime;

    public void cache(String userId, JwtToken jwtToken, CsrfToken csrfToken) {
        cacheToken(userId, jwtToken);
        cacheCsrf(userId, csrfToken);
    }

    public void verifyCache(String userId, String refreshToken, String csrfToken) {
        verifyCachedToken(userId, refreshToken);
        verifyCachedCsrf(userId, csrfToken);
    }

    public void evictCache(String userId) {
        evictCachedToken(userId);
        evictCachedCsrf(userId);
    }

    private void verifyCachedToken(String userId, String refreshToken) {
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

    private void verifyCachedCsrf(String userId, String csrfToken) {
        String key = generateKeyAboutCsrf(userId);
        try {
            String savedToken = redisTemplate.opsForValue().get(key);
            if (savedToken == null) {
                log.warn("No csrf token found for user '{}'", userId);
                throw new TokenStorageException(ErrorCode400.CSRF_VALIDATION_FAILURE);
            }
            if (!savedToken.equals(csrfToken)) {
                log.warn("CSRF token does not match stored token '{}'", csrfToken);
                throw new TokenStorageException(ErrorCode400.CSRF_VALIDATION_FAILURE);
            }
        } catch (Exception e) {
            throw new TokenStorageException(ErrorCode400.CSRF_VALIDATION_FAILURE);
        }
    }

    private void cacheToken(String userId, JwtToken jwtToken) {
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

    private void cacheCsrf(String userId, CsrfToken csrfToken) {
        String key = generateKeyAboutCsrf(userId);
        try {
            redisTemplate.opsForValue().set(
                    key,
                    csrfToken.getToken(),
                    refreshTokenValidityTime,
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            log.error("Failed to rotate csrf token for user '{}': {}", userId, e.getMessage());
            throw new TokenStorageException(ErrorCode400.CSRF_CACHE_FAILURE);
        }
    }

    private void evictCachedToken(String userId) {
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

    private void evictCachedCsrf(String userId) {
        String key = generateKeyAboutCsrf(userId);
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

    private String generateKey(String userId) {
        return REFRESH_TOKEN_PREFIX + userId;
    }

    private String generateKeyAboutCsrf(String userId) {
        return CSRF_TOKEN_PREFIX + userId;
    }
}