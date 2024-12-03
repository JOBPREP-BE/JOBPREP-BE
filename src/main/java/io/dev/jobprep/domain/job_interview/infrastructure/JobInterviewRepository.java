package io.dev.jobprep.domain.job_interview.infrastructure;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobInterviewRepository extends JpaRepository<JobInterview, Long> {
}
