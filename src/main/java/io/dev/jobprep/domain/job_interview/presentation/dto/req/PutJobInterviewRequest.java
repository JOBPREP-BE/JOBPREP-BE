package io.dev.jobprep.domain.job_interview.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutJobInterviewRequest {
    @Schema(description = "수정할 유형", example = "question", implementation = String.class)
    private final String field;
    @Schema(description = "수정할 내용", example = "당신의 장점은", implementation = String.class)
    private final String content;

}
