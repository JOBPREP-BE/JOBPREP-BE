package io.dev.jobprep.domain.job_interview.presentation;

import io.dev.jobprep.common.auth.JwtToken;
import io.dev.jobprep.common.base.CursorPaginationResult;
import io.dev.jobprep.common.base.LongCursorPaginationReq;
import io.dev.jobprep.common.swagger.template.JobInterviewSwagger;
import io.dev.jobprep.domain.job_interview.application.JobInterviewService;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.FindJobInterviewResponse;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.JobInterviewIdResponse;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.UpdateJobInterviewResponse;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.domain.users.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/${springdoc.version}/interview")
@Slf4j
@RequiredArgsConstructor
public class JobInterviewController implements JobInterviewSwagger {
    private final JobInterviewService jobInterviewService;
    private final UserCommonService userCommonService;

    @PostMapping
    public ResponseEntity<JobInterviewIdResponse> save (
            @JwtToken Long userId
    ) {
        User user = userCommonService.getUserWithId(userId);
        JobInterviewIdResponse id = jobInterviewService.saveJobInterview(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PatchMapping("/{interviewId}/{field}")
    public ResponseEntity<UpdateJobInterviewResponse> update (
            @PathVariable("interviewId") Long id, @PathVariable String field,
            @RequestBody PutJobInterviewRequest dto,
            @JwtToken Long userId
            ) {
        User user = userCommonService.getUserWithId(userId);
        return ResponseEntity.ok(UpdateJobInterviewResponse.from(jobInterviewService.update(dto, id, field, user)));
    }

    @DeleteMapping("/{interviewId}")
    public ResponseEntity<Void> delete (
            @PathVariable("interviewId") Long interviewId,
            @JwtToken Long userId
    ) {
        User user = userCommonService.getUserWithId(userId);
        jobInterviewService.delete(interviewId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<CursorPaginationResult<FindJobInterviewResponse>> find (
            @JwtToken Long userId,
            @Valid @ModelAttribute LongCursorPaginationReq pageable
            ) {
        User user = userCommonService.getUserWithId(userId);

        return ResponseEntity.ok(CursorPaginationResult.fromDataWithExtraItemForNextCheck(
                jobInterviewService.find(user, pageable.getCursorId(), pageable.getPageSize())
                        .stream()
                        .map(FindJobInterviewResponse::from)
                        .toList(),
                pageable.getPageSize())
        );
    }
}
