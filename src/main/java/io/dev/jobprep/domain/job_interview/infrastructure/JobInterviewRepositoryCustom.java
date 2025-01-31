package io.dev.jobprep.domain.job_interview.infrastructure;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;

import java.util.List;

public interface JobInterviewRepositoryCustom {
    List<JobInterview> findByConditionWithPagination(
            Long userId, Long cursorId, int pageSize
    );
}
