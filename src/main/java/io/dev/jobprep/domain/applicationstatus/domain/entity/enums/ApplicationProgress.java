package io.dev.jobprep.domain.applicationstatus.domain.entity.enums;

import static io.dev.jobprep.exception.code.ErrorCode400.INVALID_APPLICATION_PROGRESS_ARG;

import io.dev.jobprep.domain.applicationstatus.exception.ApplicationStatusException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ApplicationProgress {

    NOT_STARTED("진행 전"),
    IN_PROGRESS("진행 중"),
    SUCCEED("합격"),
    FAILED("탈락"),
    ;

    private final String description;

    ApplicationProgress(String description) {
        this.description = description;
    }

    public static ApplicationProgress from(String description) {
        if (description == null) return null;
        return Arrays.stream(ApplicationProgress.values())
            .filter(progress -> progress.getDescription().equals(description))
            .findFirst()
            .orElseThrow(() -> new ApplicationStatusException(INVALID_APPLICATION_PROGRESS_ARG));
    }
}
