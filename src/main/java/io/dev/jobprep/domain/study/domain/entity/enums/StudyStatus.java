package io.dev.jobprep.domain.study.domain.entity.enums;

import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_FINISHED_STUDY;
import static io.dev.jobprep.exception.code.ErrorCode400.INVALID_STUDY_STATUS_TO_RECRUIT;

import io.dev.jobprep.domain.study.exception.StudyException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum StudyStatus {

    RECRUITING("모집 중"),
    RECRUITMENT_CLOSED("모집 완료"),
    IN_PROGRESS("진행 중"),
    FINISHED("진행 완료");

    private final String description;

    StudyStatus(String description) {
        this.description = description;
    }

    public void validateAvailableRecruitment() {
        if (this != RECRUITING) {
            throw new StudyException(INVALID_STUDY_STATUS_TO_RECRUIT);
        }
    }

    public void validateAvailableFinish() {
        if (this != FINISHED) {
            throw new StudyException(ALREADY_FINISHED_STUDY);
        }
    }

    // 1. 관리자 삭제 ->
    // 2. 시스템 내 자동 삭제 -> '진행 완료', '모집 완료' (인원수 부족)
    public void validateAvailableDelete() {

    }

    public static StudyStatus from(String description) {
        return Arrays.stream(StudyStatus.values())
            .filter(status -> status.description.equals(description))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown description: " + description));
    }
}
