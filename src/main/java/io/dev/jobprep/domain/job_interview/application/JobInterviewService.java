package io.dev.jobprep.domain.job_interview.application;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import io.dev.jobprep.domain.job_interview.domain.enums.DefaultJobInterview;
import io.dev.jobprep.domain.job_interview.domain.enums.JobInterviewCategory;
import io.dev.jobprep.domain.job_interview.exception.JobInterviewException;
import io.dev.jobprep.domain.job_interview.infrastructure.JobInterviewRepository;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.FindJobInterviewResponse;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.JobInterviewIdResponse;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.exception.UserException;
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_DELETED_INTERVIEW;
import static io.dev.jobprep.exception.code.ErrorCode403.INTERVIEW_FORBIDDEN_OPERATION;
import static io.dev.jobprep.exception.code.ErrorCode404.INTERVIEW_NOT_FOUND;
import static io.dev.jobprep.exception.code.ErrorCode404.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobInterviewService {
    private final JobInterviewRepository jobInterviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public JobInterviewIdResponse saveJobInterview (User user) {
        JobInterview jobInterview = JobInterview.builder()
                .question("")
                .category(JobInterviewCategory.PERSONALITY)
                .answer("")
                .creator(user)
                .build();

        jobInterviewRepository.save(jobInterview);
        return JobInterviewIdResponse.from(jobInterview.getId());
    }

    @Transactional
    public FindJobInterviewResponse update (PutJobInterviewRequest request, Long id, User user) {
        JobInterview savedEntity = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new JobInterviewException(INTERVIEW_NOT_FOUND));

        validateUser(user.getId(), savedEntity.getId());
        savedEntity.update(request);

        return FindJobInterviewResponse.from(savedEntity);
    }

    @Transactional
    public void delete(Long id, User user) {
        JobInterview savedEntity = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new JobInterviewException(ALREADY_DELETED_INTERVIEW));

        validateUser(user.getId(), savedEntity.getId());

        jobInterviewRepository.delete(savedEntity);
    }

    public List<FindJobInterviewResponse> find(User user) {
        List<JobInterview> jobInterviewList = jobInterviewRepository.findAllByCreator(user);

        return jobInterviewList
                .stream()
                .map(FindJobInterviewResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void initJobInterview(User user) {

        for (DefaultJobInterview interviewList : DefaultJobInterview.values()) {
            JobInterview jobInterview = JobInterview.builder()
                    .question(interviewList.getQuestion())
                    .category(interviewList.getCategory())
                    .answer("")
                    .creator(user)
                    .build();

            jobInterviewRepository.save(jobInterview);
        }
    }

    private void validateUser(Long userId, Long jobInterviewId) {
        if (!Objects.equals(userId, jobInterviewId)) {
            throw new JobInterviewException(INTERVIEW_FORBIDDEN_OPERATION);
        }
    }
}

