package io.dev.jobprep.domain.chat.presentation;

import io.dev.jobprep.domain.chat.application.ChatWSService;
import io.dev.jobprep.domain.chat.application.dto.res.ChatMessageCommonInfo;
import io.dev.jobprep.domain.chat.presentation.dto.req.ChatMessageRequest;
import io.dev.jobprep.domain.chat.presentation.dto.res.ChatMessageCommonResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatWSController {

    private final ChatWSService chatWSService;

    // TODO: 첫 메시지를 보냄으로써 roomId가 아직 생성되어 있지 않은 경우, destination 어떻게 처리할지?
    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageCommonResponse send(
        @DestinationVariable("roomId") String roomId,
        ChatMessageRequest request
    ) {
        log.info("send chatMessage to chatRoom {} on ChatWSController", roomId);
        // TODO: handling chatMessage
        ChatMessageCommonInfo commonInfo = chatWSService.handle(
            UUID.fromString(roomId),
            request.getUserId(),
            request.getMessage()
        );
        return ChatMessageCommonResponse.from(commonInfo);
    }

    // TODO: 클라이언트의 DISCONNET 요청을 어떻게 처리할 것인지 고민

}
