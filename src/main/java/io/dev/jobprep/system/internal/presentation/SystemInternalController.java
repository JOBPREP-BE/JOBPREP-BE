package io.dev.jobprep.system.internal.presentation;

import io.dev.jobprep.scheduler.StudyAutoDeletionScheduler;
import java.time.LocalDateTime;

import io.dev.jobprep.system.internal.developer.DeveloperTokenHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequestMapping("/internal/api/v1")
@RequiredArgsConstructor
public class SystemInternalController {

    private final StudyAutoDeletionScheduler scheduler;
    private final DeveloperTokenHelper developerTokenHelper;

    @PatchMapping("/scheduler/re-run")
    public ResponseEntity<Void> reRun(@RequestHeader(AUTHORIZATION) String token) {

        // TODO: 추후에 Spring Security 와 합칠지 고민
        developerTokenHelper.verify(token);

        log.info("re-run failed scheduler for STUDY-AUTO-DELETION at {}", LocalDateTime.now());
        scheduler.executeSqlFile();
        return ResponseEntity.ok().build();
    }

}
