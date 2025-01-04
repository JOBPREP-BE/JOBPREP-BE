package io.dev.jobprep.domain.job_interview.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class JobInterviewIdResponse {
    @Schema(description = "면접 Id", example ="1", implementation = Long.class)
    @NotNull
    private final Long id;

    private JobInterviewIdResponse(Long id) {
        this.id = id;
    }

    public static JobInterviewIdResponse from(Long id) {
        return new JobInterviewIdResponse(id);
    }
}