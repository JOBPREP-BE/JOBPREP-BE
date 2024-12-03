package io.dev.jobprep.domain.job_interview.application;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import io.dev.jobprep.domain.job_interview.domain.enums.DefaultJobInterview;
import io.dev.jobprep.domain.job_interview.domain.enums.JobInterviewCategory;
import io.dev.jobprep.domain.job_interview.infrastructure.JobInterviewRepository;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.FindJobInterviewResponse;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.JobInterviewIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        JobInterview savedEntity = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("면접 데이터를 찾을 수 없습니다."));

        savedEntity.update(request);

        return FindJobInterviewResponse.from(savedEntity);
    }

    public void delete(Long id) {
        JobInterview jobInterview = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("면접 데이터를 찾을 수 없습니다."));
        jobInterviewRepository.delete(jobInterview);
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

