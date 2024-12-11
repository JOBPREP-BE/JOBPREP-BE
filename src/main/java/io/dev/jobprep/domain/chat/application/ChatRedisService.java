package io.dev.jobprep.domain.chat.application;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRedisService {

    private static final String CHAT_ROOM_PREFIX = "chat:user:";
    private static final String SEPERATOR = ":";

    private final StringRedisTemplate redisTemplate;

    public void joinChatRoom(Long destId, Long userId, String sessionId) {
        String roomKey = generateKey(destId);
        redisTemplate.opsForValue().set(sessionId, generateSessionValue(userId, destId));
        redisTemplate.expire(sessionId, 1, TimeUnit.DAYS);
        redisTemplate.opsForSet().add(roomKey, generateValue(userId, sessionId));
    }

    public void leaveChatRoom(String sessionId) {
        String sessionVal = redisTemplate.opsForValue().get(sessionId);
        if (sessionVal != null && !sessionVal.isBlank()) {
            Long userId = parseUserIdToSessionId(sessionVal);
            Long destId = parseDestIdToSessionId(sessionVal);
            redisTemplate.opsForSet().remove(generateKey(destId), generateValue(userId, sessionId));
        }
    }

    public Set<String> getUsersInChatRoom(Long destId) {
        String roomKey = generateKey(destId);
        return redisTemplate.opsForSet().members(roomKey);
    }

    public int getAmountsOfUsersInChatROom(Long destId) {
        return getUsersInChatRoom(destId).size();
    }

    public boolean isUserInChatRoom(Long destId, Long userId) {
        String roomKey = generateKey(destId);
        Set<String> values = redisTemplate.opsForSet().members(roomKey);

        return values != null && values
            .stream().anyMatch(value -> !userId.equals(parseUserId(value)));
    }

    private String generateKey(Long destId) {
        return CHAT_ROOM_PREFIX + destId.toString();
    }

    private String generateValue(Long userId, String sessionId) {
        return userId.toString() + SEPERATOR + sessionId;
    }

    private String generateSessionValue(Long userId, Long destId) {
        return userId.toString() + SEPERATOR + destId.toString();
    }

    private Long parseUserId(String value) {
        String[] token = value.split(SEPERATOR);
        return Long.parseLong(token[0]);
    }

    private String parseSessionId(String value) {
        String[] token = value.split(SEPERATOR);
        return token[1];
    }

    private Long parseDestIdToSessionId(String sessionValue) {
        String[] token = sessionValue.split(SEPERATOR);
        return Long.parseLong(token[1]);
    }

    private Long parseUserIdToSessionId(String sessionValue) {
        String[] token = sessionValue.split(SEPERATOR);
        return Long.parseLong(token[0]);
    }

}
