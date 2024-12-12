package io.dev.jobprep.domain.chat.presentation.dto.res;

import io.dev.jobprep.domain.chat.application.dto.res.ChatMessageCommonInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "채팅 메시지 응답 Dto")
@Getter
public class ChatMessageCommonResponse {


    private final Long id;

    @Schema(description = "채팅 발신자 정보")
    private final ChatUserCommonResponse chatSender;

    @Schema(description = "채팅 메시지", example = "안녕하세요! 문의 드립니다!", implementation = String.class)
    private final String message;

    @Schema(description = "채팅 메시지 생성일자", example = "2024-12-25T16:00:00", implementation = LocalDateTime.class)
    private final LocalDateTime createdAt;

    @Builder
    private ChatMessageCommonResponse(
        Long id,
        ChatUserCommonResponse chatSender,
        String message,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.chatSender = chatSender;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static ChatMessageCommonResponse from(ChatMessageCommonInfo commonInfo) {
        return ChatMessageCommonResponse.builder()
            .id(commonInfo != null ? commonInfo.getId() : null)
            .chatSender(commonInfo != null ? ChatUserCommonResponse.from(commonInfo.getSenderInfo()) : null)
            .message(commonInfo != null ? commonInfo.getMessage() : null)
            .createdAt(commonInfo != null ? commonInfo.getCreatedAt() : null)
            .build();
    }

}
