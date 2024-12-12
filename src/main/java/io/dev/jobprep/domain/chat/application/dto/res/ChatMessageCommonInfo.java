package io.dev.jobprep.domain.chat.application.dto.res;

import static io.dev.jobprep.exception.code.ErrorCode400.NON_GATHERED_CHAT_USER;

import io.dev.jobprep.domain.chat.domain.entity.document.ChatMessage;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.users.application.dto.res.UserCommonInfo;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageCommonInfo {

    private final Long id;

    private final UserCommonInfo senderInfo;

    private final String message;

    private final LocalDateTime createdAt;

    @Builder
    private ChatMessageCommonInfo(
        Long id,
        UserCommonInfo senderInfo,
        String message,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.senderInfo = senderInfo;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static ChatMessageCommonInfo of(ChatRoom chatRoom, ChatMessage chatMessage) {
        return ChatMessageCommonInfo.builder()
            .id(chatMessage != null ? chatMessage.getId() : null)
            .senderInfo(chatMessage != null ?
                UserCommonInfo.from(chatRoom.getUsers().get(getSenderIdx(chatRoom,
                    chatMessage.getSenderId()))) : null)
            .message(chatMessage != null ? chatMessage.getMessage() : null)
            .createdAt(chatMessage != null ? chatMessage.getCreatedAt() : null)
            .build();
    }

    private static Integer getSenderIdx(ChatRoom chatRoom, Long senderId) {
        return chatRoom.getUsers().stream().filter(chatUser -> chatUser.getUserId().equals(senderId))
            .map(chatRoom.getUsers()::indexOf)
            .findFirst()
            .orElseThrow(() -> new ChatException(NON_GATHERED_CHAT_USER));
    }

}
