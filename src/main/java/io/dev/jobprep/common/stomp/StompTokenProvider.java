package io.dev.jobprep.common.stomp;

import io.dev.jobprep.domain.chat.application.ChatCommonService;
import io.dev.jobprep.domain.chat.application.ChatService;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.users.application.UserCommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static io.dev.jobprep.exception.code.ErrorCode401.AUTH_MISSING_CREDENTIALS;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompTokenProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String DESTINATION = "Destination";
    private static final String DESTINATION_PREFIX = "/topic/";
    private static final String TEMP_AUTHORIZATION = "UserId";
    private static final int PREFIX_COUNTER = 7;

    private final UserCommonService userCommonService;
    private final ChatCommonService chatCommonService;
    private final ChatService chatService;

    public void verifyHeader(StompHeaderAccessor accessor) {
        UUID roomId = verifyDestination(accessor);
        Long userId = verifyHeaderTemporary(accessor);
        connectToChatRoom(roomId, userId, accessor.getSessionId());
    }

    public void disconnectToChatRoom(String sessionId) {
        chatService.disconnectChatRoom(sessionId);
    }

    private void connectToChatRoom(UUID roomId, Long userId, String sessionId) {
        log.info("extract destId '{}' for classify chatRoom in destination on StompHeaderAccessor", roomId);
        chatService.connectChatRoom(roomId, userId, sessionId);
    }

    private UUID verifyDestination(StompHeaderAccessor stompHeaderAccessor) {

        String token = getTokenFromHeader(stompHeaderAccessor, DESTINATION);
        UUID roomId = extractRoomId(token);
        chatCommonService.getChatRoom(roomId);

        return roomId;
    }

    private Long verifyHeaderTemporary(StompHeaderAccessor stompHeaderAccessor) {

        String token = getTokenFromHeader(stompHeaderAccessor, TEMP_AUTHORIZATION);
        Long userId = Long.valueOf(token);
        userCommonService.getUserWithId(userId);

        return userId;
    }

    private UUID extractRoomId(String token) {
        if (token.startsWith(DESTINATION_PREFIX)) {
            return UUID.fromString(token.substring(PREFIX_COUNTER));
        } else {
            // TODO: 에러 핸들링
            throw new IllegalArgumentException();
        }
    }

    private String getTokenFromHeader(StompHeaderAccessor stompHeaderAccessorm, String header) {
        List<String> values =  stompHeaderAccessorm.getNativeHeader(header);
        if (values == null || values.isEmpty()) {
            throw new ChatException(AUTH_MISSING_CREDENTIALS);
        }
        String token = values.get(0);
        if (token == null || token.isBlank()) {
            throw new ChatException(AUTH_MISSING_CREDENTIALS);
        }
        return token;
    }

    // TODO: 소셜 로그인 적용 시에 userId -> authorization 변경
    private void verifyAccessToken(StompHeaderAccessor stompHeaderAccessor) {
        var values = stompHeaderAccessor.getNativeHeader(AUTHORIZATION_HEADER);
        if (values == null || values.isEmpty()) {
            throw new ChatException(AUTH_MISSING_CREDENTIALS);
        }
        String accessToken = values.get(0);
        if (accessToken == null || accessToken.isBlank()) {
            throw new ChatException(AUTH_MISSING_CREDENTIALS);
        }
        // TODO: parsing Access Token to verify
        // TODO: verify Access Token using JWT
    }
}
