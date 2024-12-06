package io.dev.jobprep.domain.experience_master_cl.presentation;

import io.dev.jobprep.common.swagger.template.ExpMasterClSwagger;
import io.dev.jobprep.domain.experience_master_cl.application.ExpMasterClService;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.req.ExpMasterClPatchRequest;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.ExpMasterClIdResponse;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.FindAllExpMasterClResponse;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.FindExpMasterClResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${springdoc.version}/master")
@RequiredArgsConstructor
public class ExpMasterClController implements ExpMasterClSwagger {
    private final ExpMasterClService expMasterClService;

    @PostMapping
    public ResponseEntity<ExpMasterClIdResponse> save (@RequestParam Long userId) {
        ExpMasterClIdResponse id = expMasterClService.save(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FindExpMasterClResponse> update (
            @PathVariable("id") Long id,
            @RequestParam Long userId,
            @RequestBody ExpMasterClPatchRequest request
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(expMasterClService.patch(id, userId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (
            @PathVariable("id") Long id,
            @RequestParam Long userId
    ) {
        expMasterClService.delete(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<FindAllExpMasterClResponse>> findAll (@RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(expMasterClService.findAll(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindExpMasterClResponse> find (
            @PathVariable("id") Long id,
            @RequestParam Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(expMasterClService.find(id, userId));
    }
}
