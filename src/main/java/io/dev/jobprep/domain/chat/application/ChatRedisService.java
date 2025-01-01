package io.dev.jobprep.domain.chat.application;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRedisService {

    private static final String CHAT_ROOM_PREFIX = "chat:room:";
    private static final String SESSION_PREFIX = "session:";
    private static final String SEPERATOR = ":";

    private final StringRedisTemplate redisTemplate;

    public void joinChatRoom(UUID roomId, Long userId, String sessionId) {
        String roomKey = generateAdaptiveKey(roomId);
        String sessionKey = generateAdaptiveKey(sessionId);

        redisTemplate.opsForValue().set(sessionKey, generateAdaptiveMetaData(userId, roomId));
        redisTemplate.expire(sessionKey, 1, TimeUnit.DAYS);

        redisTemplate.opsForSet().add(roomKey, generateAdaptiveMetaData(userId, sessionId));
        redisTemplate.expire(roomKey, 1, TimeUnit.DAYS);
    }

    public void leaveChatRoom(String sessionId) {
        String sessionMetaData = redisTemplate.opsForValue().get(sessionId);
        if (sessionMetaData != null && !sessionMetaData.isBlank()) {
            Long userId = parseUserIdToSessionMetaData(sessionMetaData);
            UUID roomId = parseRoomIdToSessionMetaData(sessionMetaData);
            redisTemplate.opsForSet().remove(generateAdaptiveKey(roomId), generateAdaptiveMetaData(userId, sessionId));
        }
    }

    public boolean isUserInChatRoom(UUID roomId, Long userId) {
        String roomKey = generateAdaptiveKey(roomId);
        Set<String> values = redisTemplate.opsForSet().members(roomKey);

        return values != null && values
            .stream().anyMatch(value -> userId.equals(parseUserIdToRoomMetaData(value)));
    }

    public void recover(String sessionId, Long userId, UUID roomId) {

        // if caching data for roomKey is missing, re-caching data for recover to checking readBy!
        String sessionKey = generateAdaptiveKey(sessionId);
        if (!redisTemplate.hasKey(sessionKey)) {
            log.info("Session-MetaData {} not found, so, recovering...", sessionId);
            redisTemplate.opsForValue().set(sessionKey, generateAdaptiveMetaData(userId, roomId));
            redisTemplate.expire(sessionKey, 1, TimeUnit.DAYS);
        }
        // in else case, redis caching is healthy! ··· (1)

        String roomKey = generateAdaptiveKey(roomId);
        String roomMetaData = generateAdaptiveMetaData(userId, sessionId);
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(roomKey, roomMetaData))) {
            log.info("Room-MetaData {} not found, so, recovering...", sessionId);
            redisTemplate.opsForSet().add(roomKey, roomMetaData);
            redisTemplate.expire(roomKey, 1, TimeUnit.DAYS);
        }
        // in else case, redis caching is healthy! ··· (2)
    }

    private String generateAdaptiveKey(Object key) {
        if (key instanceof UUID) {
            return CHAT_ROOM_PREFIX + ((UUID) key).toString();
        } else if (key instanceof String) {
            return SESSION_PREFIX + (String) key;
        } else {
            throw new UnsupportedOperationException(key.getClass().getName());
        }
    }

    private String generateAdaptiveMetaData(Long userId, Object id) {
        if (id instanceof UUID) {
            return userId.toString() + SEPERATOR + ((UUID) id).toString();
        } else if (id instanceof String) {
            return userId.toString() + SEPERATOR + (String) id;
        } else {
            throw new UnsupportedOperationException(id.getClass().getName());
        }
    }

    private Long parseUserIdToRoomMetaData(String metaData) {
        String[] token = metaData.split(SEPERATOR);
        return Long.parseLong(token[0]);
    }

    private UUID parseRoomIdToSessionMetaData(String metaData) {
        String[] token = metaData.split(SEPERATOR);
        return UUID.fromString(token[1]);
    }

    private Long parseUserIdToSessionMetaData(String metaData) {
        String[] token = metaData.split(SEPERATOR);
        return Long.parseLong(token[0]);
    }

}
