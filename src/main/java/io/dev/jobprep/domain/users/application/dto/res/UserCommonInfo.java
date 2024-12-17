package io.dev.jobprep.domain.users.application.dto.res;

import io.dev.jobprep.domain.chat.domain.entity.document.ChatUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserCommonInfo {

    @NotNull
    private final Long id;

    @NotBlank
    private final String username;

    private UserCommonInfo(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static UserCommonInfo from(ChatUser chatUser) {
        return new UserCommonInfo(chatUser.getUserId(), chatUser.getUsername());
    }

}
