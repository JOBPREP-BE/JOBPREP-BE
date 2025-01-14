package io.dev.jobprep.domain.job_interview.presentation.dto.res;

import lombok.Getter;

@Getter
public class JobInterviewIdResponse {
    private final Long id;

    private JobInterviewIdResponse(Long id) {
        this.id = id;
    }

    public static JobInterviewIdResponse from(Long id) {
        return new JobInterviewIdResponse(id);
    }
}