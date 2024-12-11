package io.dev.jobprep.domain.chat.presentation.dto.res;

import io.dev.jobprep.domain.chat.application.dto.res.ChatMessageCommonInfo;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageCommonResponse {

    private final Long id;
    private final Long senderId;
    private final String message;
    private final LocalDateTime createdAt;
    private final boolean isRead;

    @Builder
    private ChatMessageCommonResponse(
        Long id, Long senderId, String message, LocalDateTime createdAt, boolean isRead
    ) {
        this.id = id;
        this.senderId = senderId;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public static ChatMessageCommonResponse from(ChatMessageCommonInfo commonInfo) {
        return ChatMessageCommonResponse.builder()
            .id(commonInfo.getId())
            .senderId(commonInfo.getSenderInfo().getUserId())
            .message(commonInfo.getMessage())
            .createdAt(commonInfo.getCreatedAt())
            .build();
    }

}
