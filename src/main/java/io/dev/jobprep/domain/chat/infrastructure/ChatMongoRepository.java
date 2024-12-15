package io.dev.jobprep.domain.chat.infrastructure;

import io.dev.jobprep.domain.chat.domain.entity.document.ChatMessage;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.domain.entity.enums.ChatRoomStatus;
import io.dev.jobprep.util.LocalDateTimeConverter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMongoRepository {

    private final MongoTemplate mongoTemplate;

    public ChatRoom save(ChatRoom chatRoom) {
        return mongoTemplate.save(chatRoom);
    }

    public ChatMessage save(ChatMessage chatMessage) {
        return mongoTemplate.save(chatMessage);
    }

    public Optional<ChatRoom> findByRoomIdAndUserId(@NonNull UUID roomId, @NonNull Long userId) {
        Query query = new Query();
        query.addCriteria(verifyId(roomId))
             .addCriteria(verifyUserId(userId));
        return Optional.ofNullable(mongoTemplate.findOne(query, ChatRoom.class));
    }

    public Optional<ChatRoom> findByRoomId(@NonNull UUID roomId) {
        Query query = new Query();
        query.addCriteria(verifyId(roomId));
        return Optional.ofNullable(mongoTemplate.findOne(query, ChatRoom.class));
    }

    public Optional<ChatRoom> findByUserId(Long userId) {
        Query query = new Query();
        query.addCriteria(verifyUserId(userId))
             .addCriteria(verifyActive());
        return Optional.ofNullable(mongoTemplate.findOne(query, ChatRoom.class));
    }

    public List<ChatRoom> findAllActiveRooms(Long userId, String cursorId, int pageSize) {
        Query query = new Query();
        query.limit(pageSize + 1)
            .addCriteria(verifyUserId(userId))
            .with(Sort.by(Sort.Order.desc("last_message.timestamp")));

        if (!cursorId.isBlank()) {
            query.addCriteria(cursorIdConditionForChatRoom(cursorId));
        }

        return mongoTemplate.find(query, ChatRoom.class);
    }


    public List<ChatMessage> findAllMessageHistory(UUID roomId, Long cursorId, int pageSize) {
        Query query = new Query();
        query.limit(pageSize + 1)
             .addCriteria(verifyRoomId(roomId))
             .addCriteria(cursorIdCondition(cursorId))
             .with(Sort.by(Sort.Order.desc("timestamp")));
        return mongoTemplate.find(query, ChatMessage.class);
    }

    public ChatRoom getLastMessage(UUID roomId, Long userId) {
        Query query = new Query();
        query.addCriteria(verifyId(roomId))
             .addCriteria(verifyActive())
             .addCriteria(verifyLastMsgRead(userId));
        return mongoTemplate.findOne(query, ChatRoom.class);
    }

    public void markMessageAsRead(UUID roomId, Long userId) {
        Query query = new Query();
        query.addCriteria(verifyRoomId(roomId))
             .addCriteria(verifyRead(userId))
             .with(Sort.by(Sort.Order.desc("timestamp")));
        int count = mongoTemplate.find(query, ChatMessage.class).size();
        Update update = new Update().addToSet("read_by", userId);
        if (count == 1) {
            mongoTemplate.findAndModify(query, update, ChatMessage.class);
        } else if (count > 1){
            mongoTemplate.updateMulti(query, update, ChatMessage.class);
        }
    }

    public void markLastMessageAsRead(UUID roomId, Long userId) {
        Query query = new Query();
        query.addCriteria(verifyId(roomId))
            .addCriteria(verifyLastMsgRead(userId));
        Update update = new Update().addToSet("last_message.read_by", userId);
        mongoTemplate.findAndModify(query, update, ChatRoom.class);
    }

    private Criteria verifyRoomId(UUID roomId) {
        return Criteria.where("room_id").is(roomId);
    }

    private Criteria verifyId(UUID roomId) {
        return Criteria.where("_id").is(roomId);
    }

    private Criteria verifyUserId(Long userId) {
        return Criteria.where("users.user_id").in(userId);
    }

    private Criteria verifyActive() {
        return Criteria.where("chat_status").is(ChatRoomStatus.ACTIVE);
    }

    private Criteria verifyRead(Long userId) {
        return Criteria.where("read_by").nin(userId);
    }

    private Criteria verifyLastMsgRead(Long userId) {
        return Criteria.where("last_message.read_by").nin(userId);
    }

    private Criteria cursorIdCondition(Long cursorId) {
        return cursorId != null ? Criteria.where("message_id").lt(cursorId) : Criteria.where("message_id").gt(0L);
    }

    private Criteria cursorIdConditionForChatRoom(String cursorId) {
        LocalDateTime lastCursorDate = LocalDateTimeConverter.convertToUtcLDT(cursorId);
        return Criteria.where("last_message.timestamp").lt(lastCursorDate);
    }

    private Criteria verifyCursorId(Long cursorId) {
        return Criteria.where("message_id").lt(cursorId);
    }

    private boolean hasNext(Long cursorId, int pageSize, UUID roomId) {
        Query query = new Query();
        query.addCriteria(verifyRoomId(roomId));

        Query queryWithId = new Query();
        query.addCriteria(verifyRoomId(roomId))
             .addCriteria(verifyCursorId(cursorId));

        return cursorId == null ? mongoTemplate.count(query, ChatMessage.class) > pageSize
            : mongoTemplate.count(queryWithId, ChatMessage.class) > pageSize;
    }

}
