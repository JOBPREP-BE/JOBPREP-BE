package io.dev.jobprep.domain.job_interview.presentation.dto.res;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import io.dev.jobprep.domain.job_interview.domain.enums.JobInterviewCategory;
import lombok.Getter;

@Getter
public class FindJobInterviewResponse {
    private final Long id;
    private final String question;
    private final JobInterviewCategory category;
    private final String answer;
    public static FindJobInterviewResponse from(JobInterview jobInterview) {
        return new FindJobInterviewResponse(jobInterview.getId(), jobInterview.getQuestion(), jobInterview.getCategory(), jobInterview.getAnswer());
    }

    private FindJobInterviewResponse(Long id, String question, JobInterviewCategory category, String answer) {
        this.id = id;
        this.question = question;
        this.category = category;
        this.answer = answer;
    }
}
