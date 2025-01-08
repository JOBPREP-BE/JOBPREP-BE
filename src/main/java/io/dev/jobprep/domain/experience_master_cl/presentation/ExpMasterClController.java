package io.dev.jobprep.domain.experience_master_cl.presentation;

import io.dev.jobprep.common.base.CursorPaginationResult;
import io.dev.jobprep.common.base.LongCursorPaginationReq;
import io.dev.jobprep.common.swagger.template.ExpMasterClSwagger;
import io.dev.jobprep.domain.experience_master_cl.application.ExpMasterClService;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.req.ExpMasterClPatchRequest;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.ExpMasterClIdResponse;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.FindExpMasterClResponse;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.UpdateExpMasterClResponse;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.domain.users.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/${springdoc.version}/master")
@RequiredArgsConstructor
public class ExpMasterClController implements ExpMasterClSwagger {
    private final ExpMasterClService expMasterClService;
    private final UserCommonService userCommonService;

    @PostMapping
    public ResponseEntity<ExpMasterClIdResponse> save (@RequestParam Long userId) {
        User user = userCommonService.getUserWithId(userId);
        ExpMasterClIdResponse id = expMasterClService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PatchMapping("/{id}/{field}")
    public ResponseEntity<UpdateExpMasterClResponse> update (
            @PathVariable("id") Long id, @PathVariable String field,
            @RequestParam Long userId,
            @RequestBody ExpMasterClPatchRequest request
            ) {
        User user = userCommonService.getUserWithId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(UpdateExpMasterClResponse.from(expMasterClService.patch(id, field, user, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (
            @PathVariable("id") Long id,
            @RequestParam Long userId
    ) {
        User user = userCommonService.getUserWithId(userId);
        expMasterClService.delete(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<CursorPaginationResult<FindExpMasterClResponse>> findAll (
            @RequestParam Long userId, @Valid @ModelAttribute LongCursorPaginationReq pageable) {
        User user = userCommonService.getUserWithId(userId);

        return ResponseEntity.ok(CursorPaginationResult.fromDataWithExtraItemForNextCheck(
                expMasterClService.findAll(user, pageable.getCursorId(), pageable.getPageSize())
                        .stream()
                        .map(FindExpMasterClResponse::toDto)
                        .toList(),
                pageable.getPageSize()
        ));
    }

}
