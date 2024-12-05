package io.dev.jobprep.domain.job_interview.presentation.dto.res;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import io.dev.jobprep.domain.job_interview.domain.enums.JobInterviewCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class FindJobInterviewResponse {
    @Schema(description = "면접 Id", example = "1")
    private final Long id;
    @Schema(description = "면접 질문", example = "성격의 장점")
    private final String question;
    @Schema(description = "면접 유형", example = "인성", implementation = String.class)
    private final String category;
    @Schema(description = "면접 예상 답변", example = "밥을 잘 먹습니다.")
    private final String answer;
    public static FindJobInterviewResponse from(JobInterview jobInterview) {
        return new FindJobInterviewResponse(jobInterview.getId(), jobInterview.getQuestion(), jobInterview.getCategory().getMessage(), jobInterview.getAnswer());
    }

    private FindJobInterviewResponse(Long id, String question, String category, String answer) {
        this.id = id;
        this.question = question;
        this.category = category;
        this.answer = answer;
    }
}
