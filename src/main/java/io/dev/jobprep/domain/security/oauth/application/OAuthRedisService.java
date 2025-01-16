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

import static io.dev.jobprep.exception.code.ErrorCode400.TMPT_CACHE_FALIURE;
import static io.dev.jobprep.exception.code.ErrorCode400.TMPT_VALIDATION_FAILURE;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthRedisService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String TEMP_TOKEN_PREFIX = "temp:";

    public void saveTemporaryToken(String temporaryToken, OAuthUserInfo userInfo, Duration expiration) {
        try {
            String key = TEMP_TOKEN_PREFIX + temporaryToken;
            String value = objectMapper.writeValueAsString(userInfo);

            redisTemplate.opsForValue().set(key, value, expiration);
        } catch (Exception e) {
            throw new TokenStorageException(TMPT_CACHE_FALIURE);
        }
    }

    public Optional<OAuthUserInfo> getTemporaryToken(String temporaryToken) {
        String key = TEMP_TOKEN_PREFIX + temporaryToken;
        String value="";

        try {
            value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                throw new TokenStorageException(TMPT_VALIDATION_FAILURE);

            }

            OAuthUserInfo userInfo = objectMapper.readValue(value, OAuthUserInfo.class);
            try {
                redisTemplate.delete(key);
            } catch(Exception e) {
                log.error("Failed to delete temporary token from Redis: {}", key, e);
            }
            return Optional.of(userInfo);

        }catch (JsonProcessingException e) {
            log.error("Failed to deserialize user info: {}", value, e);
            throw new TokenStorageException(TMPT_VALIDATION_FAILURE);
        }catch (Exception e){
            log.error("Failed to process temporary token: {}", key, e);
            throw new TokenStorageException(TMPT_VALIDATION_FAILURE);
        }
    }

    // 기존의 refresh token 관련 메소드들...
}
