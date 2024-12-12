package io.dev.jobprep.domain.chat.application;

import static io.dev.jobprep.exception.code.ErrorCode403.CHAT_ROOM_FORBIDDEN_OPERATION;
import static io.dev.jobprep.exception.code.ErrorCode404.CHATROOM_NOT_FOUND;

import io.dev.jobprep.domain.chat.application.dto.res.ChatMessageCommonInfo;
import io.dev.jobprep.domain.chat.application.dto.res.ChatRoomCommonInfo;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatMessage;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.chat.infrastructure.ChatMongoRepository;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.domain.UserRole;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMongoRepository chatRepository;
    private final UserCommonService userCommonService;
    private final ChatRedisService redisService;

    public ChatRoom create(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User sender = getUser(userId);

        User admin = getAdmin();

        ChatRoom chatRoom = ChatRoom.of(null);
        chatRoom.join(sender);
        chatRoom.join(admin);

        return chatRoom;
    }

    public List<ChatRoomCommonInfo> getAllActiveRoomList(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User admin = getUser(userId);

        if (!validateisAdmin(userId)) {
            throw new ChatException(CHAT_ROOM_FORBIDDEN_OPERATION);
        }

        // TODO: 마지막 업데이트날짜를 기준으로 내림차순 정렬 (feat. 인덱스 적용)
        return getAllActiveRooms(userId).stream().map(
            (chatRoom) -> ChatRoomCommonInfo.from(
                chatRoom,
                userId,
                stillReadMore(chatRoom, userId)
            )
        ).toList();
    }

    public List<ChatMessageCommonInfo> getMessageHistory(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(userId);

        ChatRoom chatRoom = getChatRoom(userId);
        if (chatRoom == null) {
            return List.of(ChatMessageCommonInfo.of(null, null, userId));
        }

        return getAllMessages(userId, chatRoom.getId());
    }

    public List<ChatMessageCommonInfo> getMessageHistory(Long userId, UUID roomId) {

        User admin = getUser(userId);

        if (!validateisAdmin(userId)) {
            throw new ChatException(CHAT_ROOM_FORBIDDEN_OPERATION);
        }

        return getAllMessages(userId, roomId);
    }

    private List<ChatMessageCommonInfo> getAllMessages(Long userId, UUID roomId) {
        ChatRoom chatRoom = getChatRoom(roomId);
        return getMessages(roomId).stream().map(
            chatMessage -> ChatMessageCommonInfo.of(
                chatRoom,
                chatMessage,
                userId
            )
        ).toList();
    }

    public void disable(UUID roomId) {
        ChatRoom chatRoom = getChatRoom(roomId);
        chatRoom.disable();
    }

    public void connectChatRoom(Long destId, Long userId, String sessionId) {

        redisService.joinChatRoom(destId, userId, sessionId);
        ChatRoom chatRoom = getChatRoom(destId);
        if (chatRoom != null) {
            // 지금까지 읽지 않은 메시지를 모두 읽음 처리
            markAsRead(chatRoom.getId(), userId);
        }
    }

    public void disconnectChatRoom(String sessionId) {
        redisService.leaveChatRoom(sessionId);
    }

    public void markAsRead(UUID roomId, Long userId) {
        chatRepository.markMessageAsRead(roomId, userId);
        chatRepository.markLastMessageAsRead(roomId, userId);
    }

    public ChatRoom getChatRoom(Long userId) {
        if (chatRepository.findByUserId(userId).isEmpty()) {
            return null;
        }
        return chatRepository.findByUserId(userId).get();
    }

    private ChatRoom getChatRoom(UUID roomId) {
        return chatRepository.findByRoomId(roomId)
            .orElseThrow(() -> new ChatException(CHATROOM_NOT_FOUND));
    }

    private boolean stillReadMore(ChatRoom chatRoom, Long userId) {
        return chatRepository.getLastMessage(chatRoom.getId(), userId) != null;
    }

    private List<ChatRoom> getAllActiveRooms(Long userId) {
        return chatRepository.findAllActiveRooms(userId);
    }

    private List<ChatMessage> getMessages(UUID roomId) {
        return chatRepository.findMessageHistory(roomId);
    }

    private User getUser(Long userId) {
        return userCommonService.getUserWithId(userId);
    }

    private User getAdmin() {
        return userCommonService.getUserWithRole(UserRole.ADMIN);
    }

    private boolean validateisAdmin(Long userId) {
        return userCommonService.getUserWithId(userId).getUserRole().equals(UserRole.ADMIN);
    }

}
