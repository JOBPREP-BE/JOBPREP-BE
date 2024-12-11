package io.dev.jobprep.domain.chat.application.dto.res;

import static io.dev.jobprep.exception.code.ErrorCode400.NON_GATHERED_CHAT_USER;

import io.dev.jobprep.domain.chat.application.dto.UserInfo;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatMessage;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.exception.ChatException;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageCommonInfo {

    private final Long id;

    private final UserInfo senderInfo;

    private final UserInfo receiverInfo;

    private final String message;

    private final LocalDateTime createdAt;

    @Builder
    private ChatMessageCommonInfo(
        Long id,
        UserInfo senderInfo,
        UserInfo receiverInfo,
        String message,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.senderInfo = senderInfo;
        this.receiverInfo = receiverInfo;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static ChatMessageCommonInfo of(ChatRoom chatRoom, ChatMessage chatMessage) {
        Integer senderIdx = getUserIdx(chatRoom, chatMessage.getSenderId());
        int receiverIdx = senderIdx.equals(0) ? 1 : 0;

        return ChatMessageCommonInfo.builder()
            .id(chatMessage.getId())
            .senderInfo(UserInfo.of(chatRoom.getUsers().get(senderIdx)))
            .receiverInfo(UserInfo.of(chatRoom.getUsers().get(receiverIdx)))
            .message(chatMessage.getMessage())
            .createdAt(chatMessage.getCreatedAt())
            .build();
    }

    private static Integer getUserIdx(ChatRoom chatRoom, Long senderId) {
        return chatRoom.getUsers().stream().filter(chatUser -> chatUser.getUserId().equals(senderId))
            .map(chatRoom.getUsers()::indexOf)
            .findFirst()
            .orElseThrow(() -> new ChatException(NON_GATHERED_CHAT_USER));
    }

}
