package io.dev.jobprep.domain.chat.presentation;

import io.dev.jobprep.common.swagger.template.ChatSwagger;
import io.dev.jobprep.domain.chat.application.ChatService;
import io.dev.jobprep.domain.chat.presentation.dto.res.ChatMessageCommonResponse;
import io.dev.jobprep.domain.chat.presentation.dto.res.ChatRoomAdminResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController implements ChatSwagger {

    private final ChatService chatService;

    @GetMapping("/my")
    public ResponseEntity<List<ChatMessageCommonResponse>> getMyMessageHistory(@RequestParam Long userId) {

        return ResponseEntity.ok(chatService.getMessageHistory(userId)
            .stream()
            .map(ChatMessageCommonResponse::from)
            .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ChatMessageCommonResponse>> getUsersMessageHistoryForAdmin(
        @RequestParam Long userId, @PathVariable String id
    ) {

        return ResponseEntity.ok(chatService.getMessageHistory(userId, UUID.fromString(id))
            .stream()
            .map(ChatMessageCommonResponse::from)
            .toList());
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomAdminResponse>> getActiveChatRoomsForAdmin(
        @RequestParam Long userId
    ) {

        return ResponseEntity.ok(chatService.getAllActiveRoomList(userId)
            .stream()
            .map(ChatRoomAdminResponse::from)
            .toList());
    }


}
