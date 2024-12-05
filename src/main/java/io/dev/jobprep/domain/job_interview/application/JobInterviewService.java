package io.dev.jobprep.domain.job_interview.application;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import io.dev.jobprep.domain.job_interview.domain.enums.DefaultJobInterview;
import io.dev.jobprep.domain.job_interview.domain.enums.JobInterviewCategory;
import io.dev.jobprep.domain.job_interview.exception.JobInterviewException;
import io.dev.jobprep.domain.job_interview.infrastructure.JobInterviewRepository;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.FindJobInterviewResponse;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.JobInterviewIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_DELETED_INTERVIEW;
import static io.dev.jobprep.exception.code.ErrorCode404.INTERVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobInterviewService {
    private final JobInterviewRepository jobInterviewRepository;

    @Transactional
    public JobInterviewIdResponse saveJobInterview () {
        JobInterview jobInterview = JobInterview.builder()
                .question("")
                .category(JobInterviewCategory.PERSONALITY)
                .answer("")
                .build();

        jobInterviewRepository.save(jobInterview);
        return JobInterviewIdResponse.from(jobInterview.getId());
    }

    @Transactional
    public FindJobInterviewResponse update (PutJobInterviewRequest request, Long id) {
        JobInterviewCategory category = JobInterviewCategory.from(request.getCategory());

        JobInterview savedEntity = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new JobInterviewException(INTERVIEW_NOT_FOUND));

        savedEntity.update(request, category);

        return FindJobInterviewResponse.from(savedEntity);
    }

    public void delete(Long id) {
        JobInterview savedEntity = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new JobInterviewException(ALREADY_DELETED_INTERVIEW));
        jobInterviewRepository.delete(savedEntity);
    }

    public List<FindJobInterviewResponse> find() {
        List<JobInterview> jobInterviewList = jobInterviewRepository.findAll();
        return jobInterviewList
                .stream()
                .map(FindJobInterviewResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void initJobInterview(Long memberId) {
        for (DefaultJobInterview interviewList : DefaultJobInterview.values()) {
            JobInterview jobInterview = JobInterview.builder()
                    .question(interviewList.getQuestion())
                    .category(interviewList.getCategory())
                    .answer("")
                    .build();
            jobInterviewRepository.save(jobInterview);
        }
    }
}

