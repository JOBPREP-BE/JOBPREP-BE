package io.dev.jobprep.common.stomp;

import io.dev.jobprep.domain.chat.application.ChatCommonService;
import io.dev.jobprep.domain.chat.application.ChatService;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static io.dev.jobprep.exception.code.ErrorCode400.CHAT_MISSING_DESTINATION;
import static io.dev.jobprep.exception.code.ErrorCode401.AUTH_MISSING_CREDENTIALS;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompTokenProcessor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String DESTINATION_HEADER = "Destination";
    private static final String DESTINATION_PREFIX = "/topic/";
    private static final String TEMP_AUTHORIZATION = "UserId";

    private final UserCommonService userCommonService;
    private final ChatCommonService chatCommonService;
    private final ChatService chatService;

    public void connect(StompHeaderAccessor accessor) {
        UUID roomId = verifyDestination(accessor);
        Long userId = verifyHeaderTemporary(accessor);
        chatService.access(roomId, userId, accessor.getSessionId());
    }

    public void disconnect(String sessionId) {
        chatService.cleanUp(sessionId);
    }

    public void recoverMetaData(StompHeaderAccessor accessor) {
        UUID roomId = verifyDestinationFromSubscription(accessor);
        Long userId = verifyAccessToken(accessor);
        chatService.recaching(roomId, userId, accessor.getSessionId());
    }

    private UUID verifyDestination(StompHeaderAccessor stompHeaderAccessor) {
        String token = getTokenFromHeader(stompHeaderAccessor, DESTINATION_HEADER, CHAT_MISSING_DESTINATION);
        UUID roomId = extractRoomId(token);
        chatCommonService.getChatRoom(roomId);
        return roomId;
    }

    private UUID extractRoomId(String token) {
        if (!token.startsWith(DESTINATION_PREFIX)) {
            throw new ChatException(CHAT_MISSING_DESTINATION);
        }
        return UUID.fromString(token.substring(DESTINATION_PREFIX.length()));
    }

    private UUID verifyDestinationFromSubscription(StompHeaderAccessor stompHeaderAccessor) {
        String subscription = stompHeaderAccessor.getSubscriptionId();
        if (subscription == null || !subscription.startsWith(DESTINATION_PREFIX)) {
            throw new ChatException(CHAT_MISSING_DESTINATION);
        }
        subscription = subscription.substring(DESTINATION_PREFIX.length());
        return UUID.fromString(subscription);
    }

    private Long verifyHeaderTemporary(StompHeaderAccessor stompHeaderAccessor) {
        String token = getTokenFromHeader(stompHeaderAccessor, TEMP_AUTHORIZATION, AUTH_MISSING_CREDENTIALS);
        Long userId = Long.valueOf(token);
        userCommonService.getUserWithId(userId);
        return userId;
    }

    // TODO: 소셜 로그인 합칠 떄 변경
    private Long verifyAccessToken(StompHeaderAccessor stompHeaderAccessor) {
        String accessToken = getTokenFromHeader(stompHeaderAccessor, AUTHORIZATION_HEADER, AUTH_MISSING_CREDENTIALS);

        // TODO: parsing Access Token to verify
        // TODO: verify Access Token using JWT and return 'userId'

        return null;
    }

    private String getTokenFromHeader(StompHeaderAccessor stompHeaderAccessor, String header, ErrorCode errorCode) {
        var values = stompHeaderAccessor.getNativeHeader(header);
        if (values == null || values.isEmpty()) {
            throw new ChatException(errorCode);
        }
        String token = values.get(0);
        if (token == null || token.isBlank()) {
            throw new ChatException(errorCode);
        }
        return token;
    }
}
