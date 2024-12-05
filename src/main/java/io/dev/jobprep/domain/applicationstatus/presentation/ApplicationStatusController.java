package io.dev.jobprep.domain.applicationstatus.presentation;

import io.dev.jobprep.common.swagger.template.ApplicationStatusSwagger;
import io.dev.jobprep.domain.applicationstatus.application.ApplicationStatusService;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.req.ApplicationStatusCreateRequest;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.req.ApplicationStatusUpdateRequest;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.res.ApplicationStatusCommonResponse;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.res.ApplicationStatusIdResponse;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.res.ApplicationStatusInfoResponse;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.res.ApplicationStatusUpdateResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applicationstatus")
@RequiredArgsConstructor
public class ApplicationStatusController implements ApplicationStatusSwagger {

    private final ApplicationStatusService applicationStatusService;

    @PostMapping
    public ResponseEntity<ApplicationStatusIdResponse> create(
        @RequestParam Long userId,
        @RequestBody ApplicationStatusCreateRequest request
    ) {
        return ResponseEntity.status(201).body(
            ApplicationStatusIdResponse.of(applicationStatusService.create(userId, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long userId, @PathVariable Long id) {
        applicationStatusService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationStatusInfoResponse> getMyApplicationStatus(
        @RequestParam Long userId,
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            ApplicationStatusInfoResponse.from(applicationStatusService.get(userId, id))
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationStatusCommonResponse>> getAll(@RequestParam Long userId) {
        return ResponseEntity.ok(applicationStatusService.getAll(userId).stream()
            .map(ApplicationStatusCommonResponse::from)
            .toList());
    }

    @PatchMapping("/{id}/{field}")
    public ResponseEntity<ApplicationStatusUpdateResponse> modify(
        @RequestParam Long userId,
        @PathVariable Long id, @PathVariable String field,
        @RequestBody ApplicationStatusUpdateRequest request
    ) {
        applicationStatusService.modify(userId, id, field, request);
        return ResponseEntity.ok(ApplicationStatusUpdateResponse.from(request.getNewVal()));
    }

}
