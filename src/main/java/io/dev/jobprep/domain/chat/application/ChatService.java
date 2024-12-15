package io.dev.jobprep.domain.chat.application;

import static io.dev.jobprep.exception.code.ErrorCode400.CHAT_ROOM_ALREADY_EXIST;
import static io.dev.jobprep.exception.code.ErrorCode403.CHAT_ROOM_FORBIDDEN_OPERATION;

import io.dev.jobprep.domain.chat.application.dto.res.ChatMessageCommonInfo;
import io.dev.jobprep.domain.chat.application.dto.res.ChatRoomCommonInfo;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatMessage;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.exception.ChatException;
import io.dev.jobprep.domain.chat.infrastructure.ChatMongoRepository;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.domain.UserRole;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatCommonService chatCommonService;
    private final ChatMongoRepository chatRepository;
    private final UserCommonService userCommonService;
    private final ChatRedisService redisService;

    public ChatRoom create(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User sender = getUser(userId);

        validateAlreadyExistChatRoom(userId);

        User admin = getAdmin();

        ChatRoom chatRoom = ChatRoom.of(null);
        chatRoom.join(sender);
        chatRoom.join(admin);

        chatRepository.save(chatRoom);

        return chatRoom;
    }

    public List<ChatRoomCommonInfo> getAllActiveRoomsInfo(Long userId, String cursorId, int pageSize) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User admin = getUser(userId);

        if (!validateisAdmin(userId)) {
            throw new ChatException(CHAT_ROOM_FORBIDDEN_OPERATION);
        }

        // TODO: 마지막 업데이트날짜를 기준으로 내림차순 정렬 (feat. 인덱스 적용)
        return getAllActiveRoomsWithPagination(userId, cursorId, pageSize)
            .stream().map(
                (chatRoom) -> ChatRoomCommonInfo.from(
                    chatRoom,
                    userId,
                    stillReadMore(chatRoom, userId)
                )
            )
        .toList();
    }

    public List<ChatMessageCommonInfo> getMessageHistory(Long userId, Long cursorId, int pageSize) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(userId);

        try {
            @Nullable ChatRoom chatRoom = getChatRoom(userId);
            return getMessageHistoryForChatRoom(chatRoom, cursorId, pageSize);
        } catch (NullPointerException e) {
            return List.of(ChatMessageCommonInfo.of(null, null));
        }
    }

    public List<ChatMessageCommonInfo> getMessageHistoryForAdmin(
        Long userId, UUID roomId, Long cursorId, int pageSize
    ) {

        User admin = getUser(userId);

        if (!validateisAdmin(userId)) {
            throw new ChatException(CHAT_ROOM_FORBIDDEN_OPERATION);
        }

        ChatRoom chatRoom = getChatRoom(roomId);
        return getMessageHistoryForChatRoom(chatRoom, cursorId, pageSize);
    }

    private List<ChatMessageCommonInfo> getMessageHistoryForChatRoom(
        ChatRoom chatRoom, Long cursorId, int pageSize
    ) {
        return getMessagesHistoryWithPagination(chatRoom.getId(), cursorId, pageSize)
            .stream().map(
                chatMessage -> ChatMessageCommonInfo.of(
                    chatRoom,
                    chatMessage
                )
            )
        .toList();
    }

    public void disable(UUID roomId) {
        ChatRoom chatRoom = getChatRoom(roomId);
        chatRoom.disable();
    }

    public void connectChatRoom(UUID roomId, Long userId, String sessionId) {

        redisService.joinChatRoom(roomId, userId, sessionId);
        ChatRoom chatRoom = getChatRoom(roomId);
        markAsRead(chatRoom.getId(), userId);
    }

    public void disconnectChatRoom(String sessionId) {
        redisService.leaveChatRoom(sessionId);
    }

    public void markAsRead(UUID roomId, Long userId) {
        chatRepository.markMessageAsRead(roomId, userId);
        chatRepository.markLastMessageAsRead(roomId, userId);
    }

    private List<ChatRoom> getAllActiveRoomsWithPagination(Long userId, String cursorId, int pageSize) {
        return chatRepository.findAllActiveRooms(userId, cursorId, pageSize);
    }

    private List<ChatMessage> getMessagesHistoryWithPagination(UUID roomId, Long cursorId, int pageSize) {
        return chatRepository.findAllMessageHistory(roomId, cursorId, pageSize);
    }

    private boolean stillReadMore(ChatRoom chatRoom, Long userId) {
        return chatRepository.getLastMessage(chatRoom.getId(), userId) != null;
    }

    private void validateAlreadyExistChatRoom(Long userId) {
        if (getChatRoom(userId) != null) {
            throw new ChatException(CHAT_ROOM_ALREADY_EXIST);
        }
    }

    private ChatRoom getChatRoom(UUID roomId) {
        return chatCommonService.getChatRoom(roomId);
    }

    @Nullable
    private ChatRoom getChatRoom(Long userId) {
        return chatCommonService.getChatRoom(userId);
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
