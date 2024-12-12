package io.dev.jobprep.domain.chat.application.dto.res;

import static io.dev.jobprep.exception.code.ErrorCode400.NON_GATHERED_CHAT_USER;

import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatUser;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.users.application.dto.res.UserCommonInfo;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomCommonInfo {

    @NotNull
    private final UUID id;

    // TODO: @Valid 사용을 통한 검증
    @NotNull
    private final UserCommonInfo userInfo;

    @Nullable
    private final LocalDateTime updatedAt;

    private final boolean isRead;

    @Builder
    private ChatRoomCommonInfo(
        UUID id,
        UserCommonInfo userInfo,
        LocalDateTime updatedAt,
        boolean isRead
    ) {
        this.id = id;
        this.userInfo = userInfo;
        this.updatedAt = updatedAt;
        this.isRead = isRead;
    }

    public static ChatRoomCommonInfo from(ChatRoom chatRoom, Long adminId, boolean isRead) {
        ChatUser opposite = getUserIdx(chatRoom, adminId);

        return ChatRoomCommonInfo.builder()
            .id(chatRoom.getId())
            .userInfo(UserCommonInfo.from(opposite))
            .updatedAt(chatRoom.getLastMessage().getCreatedAt())
            .isRead(isRead)
            .build();
    }

    private static ChatUser getUserIdx(ChatRoom chatRoom, Long adminId) {
        return chatRoom.getUsers().stream().filter(chatUser -> !chatUser.getUserId().equals(adminId))
            .findFirst()
            .orElseThrow(() -> new ChatException(NON_GATHERED_CHAT_USER));
    }
}
