package io.dev.jobprep.domain.chat.domain.entity.enums;

import lombok.Getter;

@Getter
public enum ChatRoomStatus {

    ACTIVE("활성화"),
    DEACTIVE("비활성화"),
    ;

    private final String description;

    ChatRoomStatus(String description) {
        this.description = description;
    }

}
