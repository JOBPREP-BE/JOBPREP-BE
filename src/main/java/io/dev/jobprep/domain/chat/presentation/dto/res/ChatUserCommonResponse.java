package io.dev.jobprep.domain.chat.presentation.dto.res;

import io.dev.jobprep.domain.users.application.dto.res.UserCommonInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "채팅 참여자 응답 Dto")
@Getter
public class ChatUserCommonResponse {

    @Schema(description = "채팅 참여자 ID", example = "2", implementation = Long.class)
    private final Long userId;

    @Schema(description = "채팅 참여자 닉네임", example = "잡잡이", implementation = String.class)
    private final String username;

    private ChatUserCommonResponse(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static ChatUserCommonResponse from(UserCommonInfo userInfo) {
        return new ChatUserCommonResponse(userInfo.getId(), userInfo.getUsername());
    }

}
