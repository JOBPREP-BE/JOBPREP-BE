package io.dev.jobprep.domain.chat.domain.entity.document;

import io.dev.jobprep.domain.users.domain.User;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class ChatUser {

    @Field("user_id")
    private final Long userId;

    @Field("username")
    private final String username;

    private ChatUser(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static ChatUser from(User user) {
        return new ChatUser(user.getId(), user.getUsername());
    }
}
