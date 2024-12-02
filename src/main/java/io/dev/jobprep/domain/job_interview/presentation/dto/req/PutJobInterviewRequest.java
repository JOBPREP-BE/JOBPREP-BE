package io.dev.jobprep.domain.job_interview.presentation.dto.req;

import io.dev.jobprep.domain.job_interview.domain.JobInterviewCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutJobInterviewRequest {
    private final String question;
    private final JobInterviewCategory category;
    private final String answer;

}
