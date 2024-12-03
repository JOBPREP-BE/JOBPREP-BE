package io.dev.jobprep.domain.job_interview.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobInterviewCategory {
    ABILITY("역량"),
    PERSONALITY("인성");

    private final String message;

    @JsonCreator
    public static JobInterviewCategory fromValue(String category) {
        return switch (category) {
            case "역량" -> JobInterviewCategory.ABILITY;
            case "인성" -> JobInterviewCategory.PERSONALITY;
            default -> null;
        };
    }
}
