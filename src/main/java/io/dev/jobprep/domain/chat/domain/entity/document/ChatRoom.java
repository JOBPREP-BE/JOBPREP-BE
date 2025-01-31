package io.dev.jobprep.domain.chat.domain.entity.document;

import static io.dev.jobprep.exception.code.ErrorCode400.CHAT_ROOM_DISABLED;
import static io.dev.jobprep.exception.code.ErrorCode400.NON_GATHERED_CHAT_USER;

import io.dev.jobprep.domain.chat.domain.entity.enums.ChatRoomStatus;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.users.domain.User;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "chat_rooms")
@NoArgsConstructor
public class ChatRoom {

    // TODO: String 타입 UUID vs Long
    @Id
    private UUID id;

    @Field("users")
    private List<ChatUser> users;

    @Field("chat_status")
    private ChatRoomStatus status;

    @Field("last_message")
    private ChatMessage lastMessage;

    @Builder
    private ChatRoom(ChatMessage lastMessage) {
        this.id = UUID.randomUUID();
        this.users = new ArrayList<>();
        this.status = ChatRoomStatus.ACTIVE;
        this.lastMessage = lastMessage;
    }

    public void join(User user) {
        users.add(ChatUser.from(user));
    }

    public void updateLastMessage(ChatMessage chatMessage) {
        this.lastMessage = chatMessage;
    }

    public void disable() {
        validateActive();
        this.status = ChatRoomStatus.DEACTIVE;
    }

    public void validateActive() {
        if (status.equals(ChatRoomStatus.DEACTIVE)) {
            throw new ChatException(CHAT_ROOM_DISABLED);
        }
    }

    public void isGathered(User user) {
        users.stream()
            .map(chatUser -> chatUser.getUserId().equals(user.getId()))
            .findFirst()
            .orElseThrow(() -> new ChatException(NON_GATHERED_CHAT_USER));
    }

    public static ChatRoom of(ChatMessage lastMessage) {
        return ChatRoom.builder()
            .lastMessage(lastMessage)
            .build();
    }

}
