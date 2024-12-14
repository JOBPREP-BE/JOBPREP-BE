package io.dev.jobprep.domain.chat.application;

import static io.dev.jobprep.exception.code.ErrorCode404.CHATROOM_NOT_FOUND;

import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.chat.infrastructure.ChatMongoRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatCommonService {

    private final ChatMongoRepository chatRepository;

    public ChatRoom getChatRoom(UUID roomId) {
        return chatRepository.findByRoomId(roomId)
            .orElseThrow(() -> new ChatException(CHATROOM_NOT_FOUND));
    }

}
