package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.core.properties.swagger.error.SwaggerApplicationStatusErrorExamples;
import io.dev.jobprep.core.properties.swagger.error.SwaggerUserErrorExamples;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.req.ApplicationStatusCreateRequest;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.req.ApplicationStatusUpdateRequest;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.res.ApplicationStatusCommonResponse;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.res.ApplicationStatusIdResponse;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.res.ApplicationStatusInfoResponse;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.res.ApplicationStatusUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApplicationStatusSwagger {

    @Operation(summary = "지원 현황 생성", description = "사용자가 지원현황을 추가할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "지원 현황 생성 성공",
            content = @Content(schema = @Schema(implementation = ApplicationStatusIdResponse.class))),
        @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            ))
    })
    ResponseEntity<ApplicationStatusIdResponse> create(
        Long userId, @RequestBody ApplicationStatusCreateRequest request
    );

    @Operation(summary = "지원 현황 삭제", description = "사용자가 지원현황을 삭제할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "지원 현황 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E03-APPLICATIONSTATUS-001", value = SwaggerApplicationStatusErrorExamples.APPLICATION_STATUS_NOT_FOUND),
                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                }
            ))
    })
    ResponseEntity<Void> delete(Long userId, @PathVariable Long id);

    @Operation(summary = "지원 현황 조회", description = "사용자가 자신의 지원현황을 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "지원 현황 조회 성공",
            content = @Content(schema = @Schema(implementation = ApplicationStatusInfoResponse.class))),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E03-APPLICATIONSTATUS-001", value = SwaggerApplicationStatusErrorExamples.APPLICATION_STATUS_NOT_FOUND),
                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                }
            ))
    })
    ResponseEntity<ApplicationStatusInfoResponse> getMyApplicationStatus(
        Long userId, @PathVariable Long id
    );

    @Operation(summary = "모든 지원 현황 리스트 조회", description = "사용자가 자신의 지원현황 리스트를 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "지원 현황 리스트 조회 성공"),
        @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            ))
    })
    ResponseEntity<List<ApplicationStatusCommonResponse>> getAll(Long userId);

    @Operation(summary = "지원 현황 수정", description = "사용자가 자신의 지원 현황을 수정할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "지원 현황 수정 성공",
            content = @Content(schema = @Schema(implementation = ApplicationStatusUpdateResponse.class))),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E03-APPLICATIONSTATUS-001", value = SwaggerApplicationStatusErrorExamples.APPLICATION_STATUS_NOT_FOUND),
                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                }
            ))
    })
    ResponseEntity<ApplicationStatusUpdateResponse> modify(
        Long userId,
        @PathVariable Long id,
        @PathVariable String field,
        @RequestBody ApplicationStatusUpdateRequest request
    );

}
