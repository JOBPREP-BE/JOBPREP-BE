package io.dev.jobprep.domain.job_interview.presentation;

import io.dev.jobprep.common.swagger.template.JobInterviewSwagger;
import io.dev.jobprep.domain.job_interview.application.JobInterviewService;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.FindJobInterviewResponse;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.JobInterviewIdResponse;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.domain.users.domain.User;
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
    private final UserCommonService userCommonService;

    @PostMapping
    public ResponseEntity<JobInterviewIdResponse> save (
            @RequestParam Long userId
    ) {
        User user = userCommonService.getUserWithId(userId);
        JobInterviewIdResponse id = jobInterviewService.saveJobInterview(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{interviewId}")
    public ResponseEntity<FindJobInterviewResponse> update (
            @PathVariable("interviewId") Long id,
            @RequestBody PutJobInterviewRequest dto,
            @RequestParam Long userId
            ) {
        User user = userCommonService.getUserWithId(userId);
        return ResponseEntity.ok(jobInterviewService.update(dto, id, user));
    }

    @DeleteMapping("/{interviewId}")
    public ResponseEntity<Void> delete (
            @PathVariable("interviewId") Long interviewId,
            @RequestParam Long userId
    ) {
        User user = userCommonService.getUserWithId(userId);
        jobInterviewService.delete(interviewId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<FindJobInterviewResponse>> find (
            @RequestParam Long userId
    ) {
        User user = userCommonService.getUserWithId(userId);
        return ResponseEntity.ok(jobInterviewService.find(user));
    }
}
