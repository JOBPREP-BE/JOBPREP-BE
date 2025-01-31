package io.dev.jobprep.core.configuration;

import io.dev.jobprep.domain.chat.interceptor.StompInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompInterceptor stompInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue")
                // TODO: heart-beat 값 설정, 현재 임시 값 선정
                .setTaskScheduler(taskScheduler())
                .setHeartbeatValue(new long[]{10000, 20000});
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompInterceptor);
    }

    private TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // TODO: 추가 설정할 부분이 있을지 점검
        taskScheduler.setThreadNamePrefix("MessageBroker-Scheduler-");
        // ThreadPoolTaskScheduler 설정을 마친 후 내부적으로 ThreadPoolExecutor를 초기화함 -> 스케줄러가 작업을 실행할 수 있는 준비 상태
        taskScheduler.initialize();
        return taskScheduler;
    }
}
