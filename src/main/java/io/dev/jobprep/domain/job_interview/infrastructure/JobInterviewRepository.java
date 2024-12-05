package io.dev.jobprep.domain.job_interview.infrastructure;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import io.dev.jobprep.domain.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobInterviewRepository extends JpaRepository<JobInterview, Long> {
    List<JobInterview> findAllByCreator(User user);
}
