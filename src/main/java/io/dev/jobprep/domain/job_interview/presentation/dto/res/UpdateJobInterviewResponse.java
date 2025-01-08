package io.dev.jobprep.domain.job_interview.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "면접 레코드 수정 응답 DTO")
@Getter
public class UpdateJobInterviewResponse {
    @Schema(description = "변경된 값", implementation = String.class)
    @NotNull
    private final String newVal;

    private UpdateJobInterviewResponse(String newVal) {
        this.newVal = newVal;
    }

    public static UpdateJobInterviewResponse from(String newVal) {
        return new UpdateJobInterviewResponse(newVal);
    }
}
