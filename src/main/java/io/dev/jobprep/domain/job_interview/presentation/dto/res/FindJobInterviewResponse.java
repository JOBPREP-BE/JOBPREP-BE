package io.dev.jobprep.domain.job_interview.presentation.dto.res;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import io.dev.jobprep.domain.job_interview.domain.JobInterviewCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindJobInterviewResponse {
    private Long id;
    private String question;
    private JobInterviewCategory category;
    private String answer;
    public static FindJobInterviewResponse from(JobInterview jobInterview) {
        return new FindJobInterviewResponse(jobInterview.getId(), jobInterview.getQuestion(), jobInterview.getCategory(), jobInterview.getAnswer());
    }
}
