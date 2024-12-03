package io.dev.jobprep.domain.applicationstatus.domain.entity.enums;

import static io.dev.jobprep.exception.code.ErrorCode400.INVALID_APPLICATION_PROCESS_ARG;

import io.dev.jobprep.domain.applicationstatus.exception.ApplicationStatusException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ApplicationProcess {

    DOCUMENT_SCREENING("서류 전형"),
    APTITUDE_CODING_TEST("인적성/코테"),
    FIRST_INTERVIEW("1차 면접"),
    FINAL_INTERVIEW("2차 면접"),
    ;

    private final String description;

    ApplicationProcess(String description) {
        this.description = description;
    }

    public static ApplicationProcess from(String description) {
        return Arrays.stream(ApplicationProcess.values())
            .filter(process -> process.getDescription().equals(description))
            .findFirst()
            .orElseThrow(() -> new ApplicationStatusException(INVALID_APPLICATION_PROCESS_ARG));
    }

}
