package io.dev.jobprep.domain.chat.application.dto;

import io.dev.jobprep.domain.chat.domain.entity.document.ChatUser;
import lombok.Getter;

@Getter
public class UserInfo {

    private final Long userId;

    private final String username;

    private UserInfo(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static UserInfo of(ChatUser chatUser) {
        return new UserInfo(chatUser.getUserId(), chatUser.getUsername());
    }

}
