package io.dev.jobprep.domain.chat.infrastructure;

import io.dev.jobprep.domain.chat.domain.entity.document.ChatMessage;
import io.dev.jobprep.domain.chat.domain.entity.document.ChatRoom;
import io.dev.jobprep.domain.chat.domain.entity.enums.ChatRoomStatus;
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
        query.addCriteria(verifyRoomId(roomId))
             .addCriteria(verifyUserId(userId));
        return Optional.ofNullable(mongoTemplate.findOne(query, ChatRoom.class));
    }

    public Optional<ChatRoom> findByRoomId(UUID roomId) {
        Query query = new Query();
        query.addCriteria(verifyRoomId(roomId));
        return Optional.ofNullable(mongoTemplate.findOne(query, ChatRoom.class));
    }

    public Optional<ChatRoom> findByUserId(Long userId) {
        Query query = new Query();
        query.addCriteria(verifyUserId(userId))
             .addCriteria(verifyActive());
        return Optional.ofNullable(mongoTemplate.findOne(query, ChatRoom.class));
    }

    public List<ChatRoom> findAllActiveRooms(Long userId) {
        Query query = new Query();
        query.addCriteria(verifyUserId(userId))
             .addCriteria(verifyActive());
        return mongoTemplate.find(query, ChatRoom.class);
    }

    public List<ChatMessage> findMessageHistory(UUID roomId) {
        Query query = new Query();
        query.addCriteria(verifyRoomId(roomId))
             .with(Sort.by(Sort.Order.desc("timestamp")));
        return mongoTemplate.find(query, ChatMessage.class);
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
        return Criteria.where("users.user_id").is(userId);
    }

    private Criteria verifyActive() {
        return Criteria.where("status").is(ChatRoomStatus.ACTIVE);
    }

    private Criteria verifyRead(Long userId) {
        return Criteria.where("read_by").nin(userId);
    }

    private Criteria verifyLastMsgRead(Long userId) {
        return Criteria.where("last_message.read_by").nin(userId);
    }

}
