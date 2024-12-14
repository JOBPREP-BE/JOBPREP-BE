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
        redisTemplate.opsForValue().set(sessionId, generateSessionValue(userId, roomId));
        redisTemplate.expire(sessionId, 1, TimeUnit.DAYS);
        redisTemplate.opsForSet().add(generateKey(roomId), generateValue(userId, sessionId));
    }

    public void leaveChatRoom(String sessionId) {
        String sessionVal = redisTemplate.opsForValue().get(sessionId);
        if (sessionVal != null && !sessionVal.isBlank()) {
            Long userId = parseUserIdToSessionId(sessionVal);
            UUID roomId = parseRoomIdToSessionId(sessionVal);
            redisTemplate.opsForSet().remove(generateKey(roomId), generateValue(userId, sessionId));
        }
    }

    public Set<String> getUsersInChatRoom(UUID roomId) {
        String roomKey = generateKey(roomId);
        return redisTemplate.opsForSet().members(roomKey);
    }

    public int getAmountsOfUsersInChatROom(UUID roomId) {
        return getUsersInChatRoom(roomId).size();
    }

    public boolean isUserInChatRoom(UUID roomId, Long userId) {
        String roomKey = generateKey(roomId);
        Set<String> values = redisTemplate.opsForSet().members(roomKey);

        return values != null && values
            .stream().anyMatch(value -> userId.equals(parseUserId(value)));
    }

    private String generateKey(UUID roomId) {
        return CHAT_ROOM_PREFIX + roomId.toString();
    }

    private String generateValue(Long userId, String sessionId) {
        return userId.toString() + SEPERATOR + sessionId;
    }

    private String generateSessionValue(Long userId, UUID roomId) {
        return userId.toString() + SEPERATOR + roomId.toString();
    }

    private Long parseUserId(String value) {
        String[] token = value.split(SEPERATOR);
        return Long.parseLong(token[0]);
    }

    private String parseSessionId(String value) {
        String[] token = value.split(SEPERATOR);
        return token[1];
    }

    private UUID parseRoomIdToSessionId(String sessionValue) {
        String[] token = sessionValue.split(SEPERATOR);
        return UUID.fromString(token[1]);
    }

    private Long parseUserIdToSessionId(String sessionValue) {
        String[] token = sessionValue.split(SEPERATOR);
        return Long.parseLong(token[0]);
    }

}
