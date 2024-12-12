package io.dev.jobprep.domain.chat.presentation.dto.res;

import io.dev.jobprep.domain.chat.application.dto.res.ChatRoomCommonInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "활성화된 채팅방 리스트 응답 Dto. 관리자가 자신의 채팅방 리스트를 확인할 때 사용한다.")
@Getter
public class ChatRoomAdminResponse {

    private final UUID roomId;

    private final ChatUserCommonResponse chatSender;

    private final LocalDateTime updatedAt;

    private final boolean isRead;

    @Builder
    private ChatRoomAdminResponse(
        UUID roomId,
        ChatUserCommonResponse chatSender,
        LocalDateTime updatedAt,
        boolean isRead
    ) {
        this.roomId = roomId;
        this.chatSender = chatSender;
        this.updatedAt = updatedAt;
        this.isRead = isRead;
    }

    public static ChatRoomAdminResponse from(ChatRoomCommonInfo chatInfo) {
        return ChatRoomAdminResponse.builder()
            .roomId(chatInfo.getId())
            .chatSender(ChatUserCommonResponse.from(chatInfo.getUserInfo()))
            .updatedAt(chatInfo.getUpdatedAt())
            .isRead(chatInfo.isRead())
            .build();
    }

}
