package io.dev.jobprep.domain.chat.application;

import static io.dev.jobprep.exception.code.ErrorCode404.CHATROOM_NOT_FOUND;

import io.dev.jobprep.domain.chat.application.dto.res.ChatMessageCommonInfo;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatMessage;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.chat.infrastructure.ChatMongoRepository;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.domain.UserRole;
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
    private final ChatService chatService;
    private final ChatRedisService redisService;
    private final SequenceGenerator generator;

    public ChatMessageCommonInfo handle(Long destId, Long userId, String message) {

        User user = userCommonService.getUserWithId(userId);
        ChatRoom chatRoom = getChatRoom(user, destId);
        chatRoom.validateActive();

        UUID roomId = chatRoom.getId();
        ChatMessage chatMessage = ChatMessage.of(
            generator.getNextSequence(roomId.toString()),
            chatRoom.getId(),
            userId,
            message
        );
        chatMessage.addReader(userId);

        // 만약, 상대방이 채팅방에 접속 중이라면 -> 전송하려는 메시지를 읽음 처리
        Long oppositeId = getOppositeId(destId, userId);
        if (redisService.isUserInChatRoom(destId, userId)) {
            markAsRead(chatMessage, oppositeId);
        }

        chatRoom.updateLastMessage(chatMessage);
        chatRepository.save(chatRoom);
        chatRepository.save(chatMessage);

        return ChatMessageCommonInfo.of(chatRoom, chatMessage, userId);
    }

    private void markAsRead(ChatMessage chatMessage, Long readerId) {
        chatMessage.addReader(readerId);
    }

    private Long getOppositeId(Long destId, Long userId) {
        if (destId.equals(userId)) {
            return userCommonService.getUserWithRole(UserRole.ADMIN).getId();
        }
        return destId;
    }

    private ChatRoom getChatRoom(User user, Long userId) {
        if (isAdmin(user)) {
            return getOppositeChatRoom(userId);
        }
        return getChatRoom(user.getId());
    }

    private ChatRoom getChatRoom(Long userId) {
        if (isFirstMessage(userId)) {
            return chatService.create(userId);
        }
        return chatRepository.findByUserId(userId).get();
    }

    private ChatRoom getOppositeChatRoom(Long userId) {
        return chatRepository.findByUserId(userId)
            .orElseThrow(() -> new ChatException(CHATROOM_NOT_FOUND));
    }

    private boolean isFirstMessage(Long userId) {
        return chatRepository.findByUserId(userId).isEmpty();
    }

    private boolean isAdmin(User user) {
        return user.getUserRole().equals(UserRole.ADMIN);
    }

}
