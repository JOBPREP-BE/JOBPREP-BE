package io.dev.jobprep.system.internal.presentation;

import io.dev.jobprep.scheduler.StudyAutoDeletionScheduler;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/internal/api/v1")
@RequiredArgsConstructor
public class SystemInternalController {

    private final StudyAutoDeletionScheduler scheduler;

    @PatchMapping("/scheduler/re-run")
    public ResponseEntity<Void> reRun() {
        log.info("re-run failed scheduler for STUDY-AUTO-DELETION at {}", LocalDateTime.now());
        scheduler.executeSqlFile();
        return ResponseEntity.ok().build();
    }

}
