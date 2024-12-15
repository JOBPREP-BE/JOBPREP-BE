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
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_DELETED_INTERVIEW;
import static io.dev.jobprep.exception.code.ErrorCode400.IS_DEFAULT_INTERVIEW;
import static io.dev.jobprep.exception.code.ErrorCode403.INTERVIEW_FORBIDDEN_OPERATION;
import static io.dev.jobprep.exception.code.ErrorCode404.INTERVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class JobInterviewService {
    private final JobInterviewRepository jobInterviewRepository;
    private final String NOT_MODIFY_FIELD = "question";

    @Transactional
    public JobInterviewIdResponse saveJobInterview (User user) {
        log.info("????????????????");
        JobInterview jobInterview = JobInterview.builder()
                .question("")
                .category(JobInterviewCategory.PERSONALITY)
                .answer("")
                .creator(user)
                .isDefault(false)
                .build();

        jobInterviewRepository.save(jobInterview);
        return JobInterviewIdResponse.from(jobInterview.getId());
    }

    @Transactional
    public FindJobInterviewResponse update (PutJobInterviewRequest request, Long id, User user) {
        JobInterview savedEntity = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new JobInterviewException(INTERVIEW_NOT_FOUND));


        validateIsDefault(savedEntity, request);
        validateUser(user.getId(), savedEntity.getCreator().getId());
        savedEntity.update(request);

        return FindJobInterviewResponse.from(savedEntity);
    }

    @Transactional
    public void delete(Long id, User user) {
        JobInterview savedEntity = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new JobInterviewException(ALREADY_DELETED_INTERVIEW));

        validateIsDefault(savedEntity);
        validateUser(user.getId(), savedEntity.getCreator().getId());

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
        log.info("initJobInterview 실행");

        for (DefaultJobInterview interviewList : DefaultJobInterview.values()) {
            JobInterview jobInterview = JobInterview.builder()
                    .question(interviewList.getQuestion())
                    .category(interviewList.getCategory())
                    .answer("")
                    .creator(user)
                    .isDefault(true)
                    .build();

            jobInterviewRepository.save(jobInterview);
            log.info("init jobInterview" + jobInterview.getId());
        }
    }

    private void validateUser(Long userId, Long jobInterviewId) {
        if (!Objects.equals(userId, jobInterviewId)) {
            throw new JobInterviewException(INTERVIEW_FORBIDDEN_OPERATION);
        }
    }

    private void validateIsDefault(JobInterview savedEntity, PutJobInterviewRequest request) {
        if (savedEntity.getIsDefault() && request.getField().equals(NOT_MODIFY_FIELD)) {
            throw new JobInterviewException(IS_DEFAULT_INTERVIEW);
        }
    }

    private void validateIsDefault(JobInterview savedEntity) {
        if (savedEntity.getIsDefault()) {
            throw new JobInterviewException(IS_DEFAULT_INTERVIEW);
        }
    }
}

