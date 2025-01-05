package io.dev.jobprep.domain.chat.domain.entity.enums;

import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_COMPLETED_CHAT;

import io.dev.jobprep.domain.chat.exception.ChatException;

public enum Status {

    WAITING("대기 상태"),
    COMPLETE("전송 완료"),
    ;

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public void validateAvailableReSend() {
        if (this == COMPLETE) {
            throw new ChatException(ALREADY_COMPLETED_CHAT);
        }
    }

}
