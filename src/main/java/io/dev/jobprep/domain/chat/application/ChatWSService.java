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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatWSService {

    private final UserCommonService userCommonService;
    private final ChatMongoRepository chatRepository;
    private final ChatCommonService chatCommonService;
    private final ChatRedisService redisService;
    private final SequenceGenerator generator;

    @Transactional(value = "mongoTransactionManager")
    public ChatMessageCommonInfo handle(UUID roomId, Long userId, String message) {

        User user = userCommonService.getUserWithId(userId);
        ChatRoom chatRoom = getChatRoom(roomId);
        chatRoom.isGathered(user);
        chatRoom.validateActive();

        ChatMessage chatMessage = handleChat(chatRoom, userId, message);
        try {
            handleChatRoom(chatRoom, chatMessage);
        } catch (RuntimeException e) {
            // Unchecked Exception 발생 시, 예외의 상위 전파를 막아 트랜잭션 롤백을 방지
            log.info("Error {} while handling chat message", e.getMessage());
        }

        return ChatMessageCommonInfo.of(chatRoom, chatMessage);
    }

    @Transactional(value = "mongoTransactionManager")
    public ChatMessage handleChat(ChatRoom chatRoom, Long userId, String message) {

        UUID roomId = chatRoom.getId();

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

        return chatRepository.save(chatMessage);
    }

    @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public void handleChatRoom(ChatRoom chatRoom, ChatMessage chatMessage) {
        updateComplete(chatMessage);
        updateChatRoom(chatRoom, chatMessage);
    }

    protected void updateComplete(ChatMessage chatMessage) {
        chatMessage.complete();
        chatRepository.update(chatMessage);
    }

    protected void updateChatRoom(ChatRoom chatRoom, ChatMessage chatMessage) {
        chatRoom.updateLastMessage(chatMessage);
        chatRepository.save(chatRoom);
    }

    protected void markAsRead(ChatMessage chatMessage, Long readerId) {
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
