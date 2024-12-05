package io.dev.jobprep.domain.job_interview.domain.enums;

import io.dev.jobprep.domain.job_interview.exception.JobInterviewException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static io.dev.jobprep.exception.code.ErrorCode400.INVALID_INTERVIEW_STATUS;

@Getter
@AllArgsConstructor
public enum JobInterviewCategory {
    ABILITY("역량"),
    PERSONALITY("인성");

    private final String message;

    public static JobInterviewCategory from(String category) {
        return switch (category) {
            case "역량" -> JobInterviewCategory.ABILITY;
            case "인성" -> JobInterviewCategory.PERSONALITY;
            default -> throw new JobInterviewException(INVALID_INTERVIEW_STATUS);
        };
    }
}
