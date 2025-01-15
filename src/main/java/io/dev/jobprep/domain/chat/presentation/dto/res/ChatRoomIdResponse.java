package io.dev.jobprep.domain.chat.presentation.dto.res;

import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Getter;

@Schema(description = "채팅방 생성 응답 Dto. 생성된 채팅방 ID를 반환한다.")
@Getter
public class ChatRoomIdResponse {

    @Schema(description = "생성된 채팅방 ID", implementation = UUID.class)
    private final UUID roomId;

    private ChatRoomIdResponse(UUID roomId) {
        this.roomId = roomId;
    }

    public static ChatRoomIdResponse from(ChatRoom chatRoom) {
        return new ChatRoomIdResponse(chatRoom.getId());
    }

}
