package io.dev.jobprep.domain.job_interview.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "면접 레코드 수정 요청 DTO")
@Getter
@NoArgsConstructor
public class PutJobInterviewRequest {
    @Schema(description = "수정할 내용", example = "당신의 장점은", implementation = String.class)
    private String newVal;
}
