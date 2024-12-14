package io.dev.jobprep.domain.chat.interceptor;

import static io.dev.jobprep.exception.code.ErrorCode401.AUTH_MISSING_CREDENTIALS;

import io.dev.jobprep.domain.chat.application.ChatCommonService;
import io.dev.jobprep.domain.chat.application.ChatService;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.users.application.UserCommonService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TEMP_AUTHORIZATION_HEADER = "UserId";
    private static final String DESTINATION_HEADER = "Destination";
    private static final String DESTINATION_PREFIX = "/topic/";
    private static final int PREFIX_COUNTER = 7;

    private final UserCommonService userCommonService;
    private final ChatCommonService chatCommonService;
    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (Objects.nonNull(accessor.getCommand())) {
            handleMessage(accessor);
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    private void handleMessage(StompHeaderAccessor accessor) {

        switch (Objects.requireNonNull(accessor.getCommand())) {

            case CONNECT:
                log.info("CONNECT REQUEST {} at: {}", accessor.getSessionId(), LocalDateTime.now());
                // TODO: verify Access Token
                verifyHeaderToConnect(accessor);
                break;
            case SUBSCRIBE:
            case SEND:
                break;
            case DISCONNECT:
                // 클라이언트가 명시적으로 DISCONNECT 요청을 보낸 경우, 세션 삭제
                log.info("DISCONNECT REQUEST for sessionId: {}", accessor.getSessionId());
                disconnectToChatRoom(accessor.getSessionId());
        }
    }

    private void verifyHeaderToConnect(StompHeaderAccessor stompHeaderAccessor) {
        UUID roomId = verifyDestination(stompHeaderAccessor);
        Long userId = verifyHeaderTemporary(stompHeaderAccessor);
        connectToChatRoom(roomId, userId, stompHeaderAccessor.getSessionId());
    }

    private UUID verifyDestination(StompHeaderAccessor stompHeaderAccessor) {

        List<String> values = stompHeaderAccessor.getNativeHeader(DESTINATION_HEADER);
        if (values == null || values.isEmpty()) {
            throw new ChatException(AUTH_MISSING_CREDENTIALS);
        }
        String token = values.get(0);
        if (token == null || token.isBlank()) {
            throw new ChatException(AUTH_MISSING_CREDENTIALS);
        }
        UUID roomId = extractRoomId(token);
        chatCommonService.getChatRoom(roomId);

        return roomId;
    }

    private Long verifyHeaderTemporary(StompHeaderAccessor stompHeaderAccessor) {

        List<String> values = stompHeaderAccessor.getNativeHeader(TEMP_AUTHORIZATION_HEADER);
        if (values == null || values.size() == 0) {
            throw new ChatException(AUTH_MISSING_CREDENTIALS);
        }
        String token = values.get(0);
        if (token == null || token.isBlank()) {
            throw new ChatException(AUTH_MISSING_CREDENTIALS);
        }
        Long userId = Long.valueOf(token);
        userCommonService.getUserWithId(userId);

        return userId;
    }

    private void connectToChatRoom(UUID roomId, Long userId, String sessionId) {
        log.info("extract destId '{}' for classify chatRoom in destination on StompHeaderAccessor", roomId);
        chatService.connectChatRoom(roomId, userId, sessionId);
    }

    private void disconnectToChatRoom(String sessionId) {
        chatService.disconnectChatRoom(sessionId);
    }

    private UUID extractRoomId(String token) {
        if (token.startsWith(DESTINATION_PREFIX)) {
            return UUID.fromString(token.substring(PREFIX_COUNTER));
        } else {
            // TODO: 에러 핸들링
            throw new IllegalArgumentException();
        }
    }

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
