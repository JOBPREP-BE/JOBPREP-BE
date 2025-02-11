package io.dev.jobprep.domain.security.jwt.application.handler;

import org.springframework.data.redis.core.StringRedisTemplate;

public abstract class AbstractTokenCacheHandler {

    protected final StringRedisTemplate redisTemplate;

    protected AbstractTokenCacheHandler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected abstract void cache(String userId, String token);
    protected abstract void verifyCache(String userId, String token);
    protected abstract void evictCache(String userId);
    protected abstract String generateKey(String userId);

}
