package io.dev.jobprep.domain.study.presentation;

import io.dev.jobprep.common.base.CursorPaginationReq;
import io.dev.jobprep.common.base.CursorPaginationResult;
import io.dev.jobprep.common.base.OffsetPaginationReq;
import io.dev.jobprep.common.base.OffsetPaginationResult;
import io.dev.jobprep.common.swagger.template.StudySwagger;
import io.dev.jobprep.domain.study.application.StudyScheduleService;
import io.dev.jobprep.domain.study.application.StudyService;
import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyCreateRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyUpdateAdminRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyUpdateRequest;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyCommonResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyIdResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyInfoAdminResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyInfoResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyUpdateAdminResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyUpdateResponse;
import io.dev.jobprep.util.LongParsingProvider;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@RestController
public class StudyController implements StudySwagger {

    private final StudyService studyService;
    private final StudyScheduleService studyScheduleService;

    @PostMapping
    public ResponseEntity<StudyIdResponse> create(
        @RequestParam Long userId, @RequestBody StudyCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StudyIdResponse.from(studyService.create(userId, request)));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<StudyIdResponse> join(@RequestParam Long userId, @PathVariable Long id) {
        return ResponseEntity.ok(StudyIdResponse.from(studyService.join(userId, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long userId, @PathVariable Long id) {
        studyService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    public ResponseEntity<CursorPaginationResult<StudyInfoAdminResponse>> getAllForAdmin(
        @RequestParam Long userId,
        @Valid @ModelAttribute CursorPaginationReq pageable
    ) {
        Long cursorId = LongParsingProvider.provide(pageable.getCursorId());
        return ResponseEntity.ok(
            CursorPaginationResult.fromDataWithExtraItemForNextCheck(
                studyService.getAll(userId, cursorId, pageable.getPageSize())
                    .stream()
                    .map(StudyInfoAdminResponse::of)
                    .collect(Collectors.toList()),
                pageable.getPageSize()
            )
        );
    }

    @PatchMapping("/{id}/{field}/admin")
    public ResponseEntity<StudyUpdateAdminResponse> modifyForAdmin(
        @RequestParam Long userId,
        @PathVariable Long id, @PathVariable String field,
        @RequestBody StudyUpdateAdminRequest request
    ) {
        studyService.update(userId, id, field, request);
        return ResponseEntity.ok(StudyUpdateAdminResponse.of(request.getLink()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudyUpdateResponse> modify(
        @RequestParam Long userId,
        @PathVariable Long id,
        @RequestBody StudyUpdateRequest request
    ) {
        studyScheduleService.modify(userId, id, request);
        return ResponseEntity.ok(StudyUpdateResponse.of(request.getStartDate(), request.getWeekNumber()));
    }

    @GetMapping
    public ResponseEntity<OffsetPaginationResult<StudyCommonResponse>> getRecruitingStudy(
        @RequestParam Long userId,
        @Valid @ModelAttribute OffsetPaginationReq pageable
    ) {
        return ResponseEntity.ok(
            OffsetPaginationResult.fromDataWithOffsetPageInfo(
                studyService.getRecruitingStudy(userId, pageable.getPage(),
                pageable.getPageGroupSize(), pageable.getPageSize())
                    .stream().map(StudyCommonResponse::from).collect(Collectors.toList()),
                pageable.getPageSize(),
                pageable.getPage(),
                studyService.getTotalAmountsOfData()
            )
        );
    }

    @GetMapping("/my")
    public ResponseEntity<StudyInfoResponse> getMyStudy(@RequestParam Long userId) {

        Optional<Study> study = studyService.getGatheredStudy(userId);
        return study.map(value -> ResponseEntity.ok(StudyInfoResponse.of(
                studyService.getGatheredStudy(userId).get(),
                studyScheduleService.getAll(value.getId())
            )
        )).orElseGet(() -> ResponseEntity.noContent().build());
    }
}
