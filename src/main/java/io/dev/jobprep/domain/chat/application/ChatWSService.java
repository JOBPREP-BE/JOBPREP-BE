package io.dev.jobprep.domain.chat.application;

import static io.dev.jobprep.exception.code.ErrorCode404.CHATUSER_NOT_FOUND;

import io.dev.jobprep.domain.chat.application.dto.res.ChatMessageCommonInfo;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatMessage;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.chat.infrastructure.ChatMongoRepository;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.domain.users.domain.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatWSService {

    private final UserCommonService userCommonService;
    private final ChatMongoRepository chatRepository;
    private final ChatCommonService chatCommonService;
    private final ChatRedisService redisService;
    private final SequenceGenerator generator;

    public ChatMessageCommonInfo handle(UUID roomId, Long userId, String message) {

        User user = userCommonService.getUserWithId(userId);
        ChatRoom chatRoom = getChatRoom(roomId);
        chatRoom.isGathered(user);
        chatRoom.validateActive();

        ChatMessage chatMessage = ChatMessage.of(
            generator.getNextSequence(roomId.toString()),
            roomId,
            userId,
            message
        );
        chatMessage.addReader(userId);

        // 만약, 상대방이 채팅방에 접속 중이라면 -> 전송하려는 메시지를 읽음 처리
        Long oppositeId = getOppositeId(chatRoom, userId);

        if (redisService.isUserInChatRoom(roomId, oppositeId)) {
            // 사용자가 브라우저를 끄면 DISCONNECT 됨
            markAsRead(chatMessage, oppositeId);
        }

        chatRoom.updateLastMessage(chatMessage);
        chatRepository.save(chatRoom);
        chatRepository.save(chatMessage);

        return ChatMessageCommonInfo.of(chatRoom, chatMessage);
    }

    private void markAsRead(ChatMessage chatMessage, Long readerId) {
        chatMessage.addReader(readerId);
    }

    private Long getOppositeId(ChatRoom chatRoom, Long userId) {
        return chatRoom.getUsers().stream()
            .filter(chatUser -> !chatUser.getUserId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new ChatException(CHATUSER_NOT_FOUND))
            .getUserId();
    }

    private ChatRoom getChatRoom(UUID roomId) {
        return chatCommonService.getChatRoom(roomId);
    }

}
