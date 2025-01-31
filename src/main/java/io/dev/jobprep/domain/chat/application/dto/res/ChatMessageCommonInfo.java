package io.dev.jobprep.domain.chat.application.dto.res;

import static io.dev.jobprep.exception.code.ErrorCode400.NON_GATHERED_CHAT_USER;

import io.dev.jobprep.domain.chat.domain.entity.document.ChatMessage;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.domain.entity.enums.Status;
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

    private final Status status;

    private final LocalDateTime createdAt;

    @Builder
    private ChatMessageCommonInfo(
        Long id,
        UserCommonInfo senderInfo,
        String message,
        Status status,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.senderInfo = senderInfo;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static ChatMessageCommonInfo of(ChatRoom chatRoom, ChatMessage chatMessage) {

        return ChatMessageCommonInfo.builder()
            .id((Long) resolve(chatMessage, Long.class))
            .senderInfo(resolve(chatMessage, chatRoom))
            .message((String) resolve(chatMessage, String.class))
            .status((Status) resolve(chatMessage, Status.class))
            .createdAt((LocalDateTime) resolve(chatMessage, LocalDateTime.class))
            .build();
    }

    private static Object resolve(ChatMessage chatMessage, Class<?> clazz) {
        if (clazz == Long.class) {
            return chatMessage != null ? chatMessage.getId() : null;
        } else if (clazz == String.class) {
            return chatMessage != null ? chatMessage.getMessage() : null;
        } else if (clazz == Status.class) {
            return chatMessage != null ? chatMessage.getStatus() : null;
        } else if (clazz == LocalDateTime.class) {
            return chatMessage != null ? chatMessage.getCreatedAt() : null;
        } else {
            throw new UnsupportedOperationException("invalid field type!");
        }
    }

    private static UserCommonInfo resolve(ChatMessage chatMessage, ChatRoom chatRoom) {
        return chatMessage != null ?
            UserCommonInfo.from(
                chatRoom.getUsers().get(getSenderIdx(chatRoom, chatMessage.getSenderId()))
            ) : null;
    }

    private static Integer getSenderIdx(ChatRoom chatRoom, Long senderId) {
        return chatRoom.getUsers().stream().filter(chatUser -> chatUser.getUserId().equals(senderId))
            .map(chatRoom.getUsers()::indexOf)
            .findFirst()
            .orElseThrow(() -> new ChatException(NON_GATHERED_CHAT_USER));
    }

}
