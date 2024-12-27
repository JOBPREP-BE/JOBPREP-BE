package io.dev.jobprep.domain.chat.interceptor;

import io.dev.jobprep.common.stomp.StompTokenProvider;
import java.time.LocalDateTime;
import java.util.Objects;
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

    private final StompTokenProvider stompTokenProvider;

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
                stompTokenProvider.verifyHeader(accessor);
                break;
            case SUBSCRIBE:
            case SEND:
                break;
            case DISCONNECT:
                // 클라이언트가 명시적으로 DISCONNECT 요청을 보낸 경우, 세션 삭제
                log.info("DISCONNECT REQUEST for sessionId: {}", accessor.getSessionId());
                stompTokenProvider.disconnectToChatRoom(accessor.getSessionId());
        }
    }
}
