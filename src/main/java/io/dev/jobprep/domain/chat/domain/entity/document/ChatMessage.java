package io.dev.jobprep.domain.chat.domain.entity.document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "messages")
@NoArgsConstructor
public class ChatMessage {

    @Field("message_id")
    private Long id;

    @Field("room_id")
    private UUID roomId;

    @Field("sender_id")
    private Long senderId;

    @Field("content")
    private String message;

    @Field("timestamp")
    private LocalDateTime createdAt;

    @Field("read_by")
    private List<Long> readBy;

    @Builder
    private ChatMessage(Long id, UUID roomId, Long senderId, String message) {
        this.id = id;
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.readBy = new ArrayList<>();
    }

    public void addReader(Long userId) {
        if (!validateAvailableRead(userId)) {
            readBy.add(userId);
        }
    }

    private boolean validateAvailableRead(Long userId) {
        return readBy.contains(userId);
    }

    public static ChatMessage of(Long id, UUID roomId, Long senderId, String messasge) {
        return ChatMessage.builder()
            .id(id)
            .roomId(roomId)
            .senderId(senderId)
            .message(messasge)
            .build();
    }

}
