package io.dev.jobprep.domain.chat.presentation.dto.res;

import io.dev.jobprep.domain.chat.application.dto.res.ChatMessageCommonInfo;
import io.dev.jobprep.domain.chat.domain.entity.enums.Status;
import io.dev.jobprep.domain.users.application.dto.res.UserCommonInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Schema(description = "채팅 메시지 응답 Dto")
@Getter
public class ChatMessageCommonResponse {

    @Schema(description = "메시지 ID", example = "1", implementation = Long.class)
    private final Long id;

    @Schema(description = "채팅 발신자 정보")
    private final ChatUserCommonResponse chatSender;

    @Schema(description = "채팅 메시지", example = "안녕하세요! 문의 드립니다!", implementation = String.class)
    private final String message;

    @Schema(description = "메시지 상태", example = "전송 완료", implementation = String.class)
    private final String status;

    @Schema(description = "채팅 메시지 생성일자", example = "2024-12-25T16:00:00", implementation = LocalDateTime.class)
    private final LocalDateTime createdAt;

    @Builder
    private ChatMessageCommonResponse(
        Long id,
        ChatUserCommonResponse chatSender,
        String message,
        Status status,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.chatSender = chatSender;
        this.message = message;
        this.status = resolve(status);
        this.createdAt = createdAt;
    }

    private String resolve(@NonNull final Status status) {
        return status.getDescription();
    }

    public static ChatMessageCommonResponse from(ChatMessageCommonInfo commonInfo) {

        return ChatMessageCommonResponse.builder()
            .id(commonInfo.getId())
            .chatSender(resolve(commonInfo.getSenderInfo()))
            .message(commonInfo.getMessage())
            .status(commonInfo.getStatus())
            .createdAt(commonInfo.getCreatedAt())
            .build();
    }

    private static ChatUserCommonResponse resolve(UserCommonInfo senderInfo) {
        if (senderInfo == null) {
            return null;
        }
        return ChatUserCommonResponse.from(senderInfo);
    }

}
