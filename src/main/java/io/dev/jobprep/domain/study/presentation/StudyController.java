package io.dev.jobprep.domain.study.presentation;

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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@RestController
public class StudyController implements StudySwagger {

    private final StudyService studyService;
    private final StudyScheduleService studyScheduleService;

    @PostMapping
    public ResponseEntity<StudyIdResponse> create(Long userId, StudyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StudyIdResponse.from(studyService.create(userId, request)));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<StudyIdResponse> join(Long userId, @PathVariable Long id) {
        return ResponseEntity.ok(StudyIdResponse.from(studyService.join(userId, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Long userId, @PathVariable Long id) {
        studyService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    public ResponseEntity<List<StudyInfoAdminResponse>> getAllForAdmin(Long userId) {
        return ResponseEntity.ok(studyService.getAll(userId).stream()
            .map(StudyInfoAdminResponse::of)
            .collect(Collectors.toList()));
    }

    @PatchMapping("/{id}/{field}/admin")
    public ResponseEntity<StudyUpdateAdminResponse> modifyForAdmin(
        Long userId, @PathVariable Long id, @PathVariable String field, StudyUpdateAdminRequest request
    ) {
        studyService.update(userId, id, field, request);
        return ResponseEntity.ok(StudyUpdateAdminResponse.of(request.getLink()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudyUpdateResponse> modify(
        Long userId, @PathVariable Long id, StudyUpdateRequest request
    ) {
        studyScheduleService.modify(userId, id, request);
        return ResponseEntity.ok(StudyUpdateResponse.of(request.getStartDate(), request.getWeekNumber()));
    }

    @GetMapping
    public ResponseEntity<List<StudyCommonResponse>> getRecruitingStudy(Long userId) {
        return ResponseEntity.ok(studyService.getRecruitingStudy(userId)
            .stream().map(StudyCommonResponse::from).collect(Collectors.toList()));
    }

    @GetMapping("/my")
    public ResponseEntity<StudyInfoResponse> getMyStudy(Long userId) {

        Optional<Study> study = studyService.getGatheredStudy(userId);
        return study.map(value -> ResponseEntity.ok(StudyInfoResponse.of(
                studyService.getGatheredStudy(userId).get(),
                studyScheduleService.getAll(value.getId())
            )
        )).orElseGet(() -> ResponseEntity.noContent().build());
    }
}
