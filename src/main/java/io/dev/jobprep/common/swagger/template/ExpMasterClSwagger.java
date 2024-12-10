package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.core.properties.swagger.error.SwaggerExpMasterClErrorExamples;
import io.dev.jobprep.core.properties.swagger.error.SwaggerUserErrorExamples;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.req.ExpMasterClPatchRequest;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.ExpMasterClIdResponse;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.FindAllExpMasterClResponse;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.FindExpMasterClResponse;
import io.dev.jobprep.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Experience Master Cover Letter", description = "마스터 자소서 관련 API")
@SuppressWarnings("unused")
public interface ExpMasterClSwagger {
    @Operation(summary = "마스터 자소서 생성", description = "사용자가 마스터 자소서를 생성할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "마스터 자소서 생성 성공",
                    content = @Content(schema = @Schema(implementation = ExpMasterClIdResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                    )),
    })
    ResponseEntity<ExpMasterClIdResponse> save(@RequestParam Long userId);

    @Operation(summary = "마스터 자소서 삭제", description = "마스터 자소서를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마스터 자소서 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E01-EXP-MASTER-001", value = SwaggerExpMasterClErrorExamples.ALREADY_DELETED_MASTER_CL)
                    )),
            @ApiResponse(responseCode = "403", description = "마스터 자소서 삭제 권한이 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-EXP-MASTER-001", value = SwaggerExpMasterClErrorExamples.MASTER_CL_FORBIDDEN_OPERATION)
                    )),
            @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                            }
                    ))
    })
    ResponseEntity<Void> delete (@PathVariable("id") Long id, @RequestParam Long userId);

    @Operation(summary = "모든 마스터 자소서 조회", description = "모든 마스터 자소서 데이터를 조회합니다.")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "마스터 자소서 조회 성공"),
            @ApiResponse(responseCode = "403", description = "마스터 자소서 조회 권한이 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-EXP-MASTER-0012", value = SwaggerExpMasterClErrorExamples.MASTER_CL_FORBIDDEN_OPERATION)
                    )),
            @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                    )),
    })
    ResponseEntity<List<FindAllExpMasterClResponse>> findAll (@RequestParam Long userId);

    @Operation(summary = "마스터 자소서 조회", description = "모든 마스터 자소서 데이터를 조회합니다.")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "마스터 자소서 조회 성공",
                    content = @Content(schema = @Schema(implementation = FindExpMasterClResponse.class))),
            @ApiResponse(responseCode = "403", description = "마스터 자소서 조회 권한이 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-EXP-MASTER-001", value = SwaggerExpMasterClErrorExamples.MASTER_CL_FORBIDDEN_OPERATION)
                    )),
            @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                    )),
    })
    ResponseEntity<FindExpMasterClResponse> find (@PathVariable("id") Long id, @RequestParam Long userId);

    @Operation(summary = "마스터 자소서 수정", description = "마스터 자소서 데이터를 수정합니다.")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "마스터 자소서 수정 성공",
                    content = @Content(schema = @Schema(implementation = FindExpMasterClResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "E01-EXP-MASTER-001", value = SwaggerExpMasterClErrorExamples.ALREADY_DELETED_MASTER_CL)
                            }
                    )),
            @ApiResponse(responseCode = "403", description = "마스터 자소서를 수정할 권한이 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-EXP-MASTER-001", value = SwaggerExpMasterClErrorExamples.MASTER_CL_FORBIDDEN_OPERATION)
                    )),
            @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                            }
                    ))
    })
    ResponseEntity<FindExpMasterClResponse> update (
            @PathVariable("id") Long id, @RequestParam Long userId, @RequestBody ExpMasterClPatchRequest request
    );
}
