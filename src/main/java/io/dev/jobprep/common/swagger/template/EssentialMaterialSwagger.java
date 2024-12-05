package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.core.properties.swagger.error.SwaggerUserErrorExamples;
import io.dev.jobprep.domain.essentialMaterial.presentation.dto.req.EssentialMaterialIUpdateAPIRequest;
import io.dev.jobprep.domain.essentialMaterial.presentation.dto.res.EssentialMaterialGetAPIResponse;
import io.dev.jobprep.domain.essentialMaterial.presentation.dto.res.EssentialMaterialUpdateAPIResponse;
import io.dev.jobprep.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface EssentialMaterialSwagger {
    @Operation(summary = "필수 자료 열람", description = "사용자의 필수 자료를 열람할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료 열람 성공",
                    content = @Content(schema = @Schema(implementation = EssentialMaterialGetAPIResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                    )),
    })
    ResponseEntity < EssentialMaterialGetAPIResponse > get(@RequestParam(required = false) Long userId);
    @Operation(summary = "필수 자료 최신화", description = "사용자의 필수 자료를 업데이트할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료 최신화 성공",
                    content = @Content(schema = @Schema(implementation = EssentialMaterialUpdateAPIResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                    )),
    })
    ResponseEntity<EssentialMaterialUpdateAPIResponse> update(@RequestParam(required = false) Long userId,
                                                              @RequestBody EssentialMaterialIUpdateAPIRequest req);
}
