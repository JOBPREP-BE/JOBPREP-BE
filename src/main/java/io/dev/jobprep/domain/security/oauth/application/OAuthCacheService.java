package io.dev.jobprep.domain.security.oauth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dev.jobprep.domain.security.jwt.exception.TokenStorageException;
import io.dev.jobprep.domain.security.oauth.application.dto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

import static io.dev.jobprep.exception.code.ErrorCode400.OTPT_CACHE_FALIURE;
import static io.dev.jobprep.exception.code.ErrorCode400.OTPT_VALIDATION_FAILURE;


@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthCacheService {

    private static final String OTP_TOKEN_PREFIX = "otp:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void cache(String otpToken, OAuthUserInfo userInfo, Duration expiration) {
        try {
            String key = generateKey(otpToken);
            String value = objectMapper.writeValueAsString(userInfo);
            redisTemplate.opsForValue().set(key, value, expiration);
        } catch (Exception e) {
            throw new TokenStorageException(OTPT_CACHE_FALIURE);
        }
    }

    public Optional<OAuthUserInfo> fetch(String otpToken) {
        String key = generateKey(otpToken);
        Optional<String> value = Optional.empty();

        try {
            value = Optional.ofNullable(redisTemplate.opsForValue().get(key));
            if (value.isEmpty()) {
                throw new TokenStorageException(OTPT_VALIDATION_FAILURE);
            }
            OAuthUserInfo userInfo = objectMapper.readValue(value.get(), OAuthUserInfo.class);
            if (userInfo == null) {
                throw new TokenStorageException(OTPT_VALIDATION_FAILURE);
            }
            try {
                redisTemplate.delete(key);
            } catch(Exception e) {
                log.error("Failed to delete otp-token from Redis: {}", key, e);
            }
            return Optional.of(userInfo);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize user info: {}", value.get(), e);
            throw new TokenStorageException(OTPT_VALIDATION_FAILURE);
        } catch (Exception e){
            log.error("Failed to process temporary token: {}", key, e);
            throw new TokenStorageException(OTPT_VALIDATION_FAILURE);
        }
    }

    private String generateKey(String token) {
        return OTP_TOKEN_PREFIX + token;
    }
}
