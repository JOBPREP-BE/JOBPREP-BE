package io.dev.jobprep.domain.job_interview.presentation;

import io.dev.jobprep.domain.job_interview.application.JobInterviewService;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.FindJobInterviewResponse;
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
public class JobInterviewController {
    private final JobInterviewService jobInterviewService;

    @PostMapping
    public ResponseEntity<Long> save () {
        Long id = jobInterviewService.saveJobInterview();
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{interviewId}")
    public ResponseEntity<FindJobInterviewResponse> update (
            @PathVariable("interviewId") Long id,
            @RequestBody PutJobInterviewRequest dto
            ) {
        return ResponseEntity.ok(jobInterviewService.update(dto, id));
    }

    @DeleteMapping("/{interviewId}")
    public ResponseEntity<Long> delete (
            @PathVariable("interviewId") Long interviewId
    ) {
        Long id = jobInterviewService.delete(interviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(id);
    }

    @GetMapping
    public ResponseEntity<List<FindJobInterviewResponse>> find () {
        return ResponseEntity.ok(jobInterviewService.find());
    }
}
