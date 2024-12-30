package io.dev.jobprep.common.event;

import io.dev.jobprep.common.stomp.StompTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketMonitoringEventListener {

    private final StompTokenProvider stompTokenProvider;

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        log.info("Disconnected: Session Id {}", sessionId);

        stompTokenProvider.disconnectToChatRoom(sessionId);
    }

}
