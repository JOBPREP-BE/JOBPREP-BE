package io.dev.jobprep.domain.job_interview.presentation.dto.req;

import io.dev.jobprep.domain.job_interview.domain.enums.JobInterviewCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutJobInterviewRequest {
    @Schema(description = "면접 질문", example = "성격의 장점")
    private final String question;
    @Schema(description = "면접 유형", example = "인성", implementation = String.class)
    private final String category;
    @Schema(description = "면접 예상 답변", example = "밥을 잘 먹습니다.")
    private final String answer;

}
