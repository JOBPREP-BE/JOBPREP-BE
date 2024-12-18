package io.dev.jobprep.domain.chat.presentation;

import io.dev.jobprep.common.base.CursorPaginationReq;
import io.dev.jobprep.common.base.CursorPaginationResult;
import io.dev.jobprep.common.swagger.template.ChatSwagger;
import io.dev.jobprep.domain.chat.application.ChatService;
import io.dev.jobprep.domain.chat.presentation.dto.res.ChatMessageCommonResponse;
import io.dev.jobprep.domain.chat.presentation.dto.res.ChatRoomAdminResponse;
import io.dev.jobprep.domain.chat.presentation.dto.res.ChatRoomIdResponse;
import io.dev.jobprep.util.LongParsingProvider;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController implements ChatSwagger {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatRoomIdResponse> create(@RequestParam Long userId) {

        return ResponseEntity.status(201)
            .body(ChatRoomIdResponse.from(chatService.create(userId)));
    }

    @GetMapping("/my")
    public ResponseEntity<CursorPaginationResult<ChatMessageCommonResponse>> getMyMessageHistory(
        @RequestParam Long userId,
        @Valid @ModelAttribute CursorPaginationReq pageable
    ) {

        Long cursorId = LongParsingProvider.provide(pageable.getCursorId());

        return ResponseEntity.ok(CursorPaginationResult.fromDataWithExtraItemForNextCheck(
            chatService.getMessageHistory(userId, cursorId, pageable.getPageSize())
                .stream()
                .map(ChatMessageCommonResponse::from)
                .toList(),
            pageable.getPageSize()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursorPaginationResult<ChatMessageCommonResponse>> getUserMessageHistoryForAdmin(
        @RequestParam Long userId,
        @PathVariable String id,
        @Valid @ModelAttribute CursorPaginationReq pageable
    ) {
        Long cursorId = LongParsingProvider.provide(pageable.getCursorId());

        return ResponseEntity.ok(CursorPaginationResult.fromDataWithExtraItemForNextCheck(
            chatService.getMessageHistoryForAdmin(
                userId, UUID.fromString(id), cursorId, pageable.getPageSize())
                .stream()
                .map(ChatMessageCommonResponse::from)
                .toList(),
            pageable.getPageSize()
        ));
    }

    @GetMapping
    public ResponseEntity<CursorPaginationResult<ChatRoomAdminResponse>> getActiveChatRoomsForAdmin(
        @RequestParam Long userId,
        @Valid @ModelAttribute CursorPaginationReq pageable
    ) {

        return ResponseEntity.ok(CursorPaginationResult.fromDataWithExtraItemForNextCheck(
            chatService.getAllActiveRoomsInfo(userId, pageable.getCursorId(), pageable.getPageSize())
                .stream()
                .map(ChatRoomAdminResponse::from)
                .toList(),
            pageable.getPageSize()
        ));
    }


}
