package io.dev.jobprep.domain.job_interview.presentation;

import io.dev.jobprep.common.swagger.template.JobInterviewSwagger;
import io.dev.jobprep.domain.job_interview.application.JobInterviewService;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.FindJobInterviewResponse;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.JobInterviewIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${springdoc.version}/interview")
@Slf4j
@RequiredArgsConstructor
public class JobInterviewController implements JobInterviewSwagger {
    private final JobInterviewService jobInterviewService;

    @PostMapping
    public ResponseEntity<JobInterviewIdResponse> save (
            @RequestParam Long userId
    ) {
        JobInterviewIdResponse id = jobInterviewService.saveJobInterview(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{interviewId}")
    public ResponseEntity<FindJobInterviewResponse> update (
            @PathVariable("interviewId") Long id,
            @RequestBody PutJobInterviewRequest dto,
            @RequestParam Long userId
            ) {
        return ResponseEntity.ok(jobInterviewService.update(dto, id, userId));
    }

    @DeleteMapping("/{interviewId}")
    public ResponseEntity<Void> delete (
            @PathVariable("interviewId") Long interviewId,
            @RequestParam Long userId
    ) {
        jobInterviewService.delete(interviewId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<FindJobInterviewResponse>> find (
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(jobInterviewService.find(userId));
    }
}
