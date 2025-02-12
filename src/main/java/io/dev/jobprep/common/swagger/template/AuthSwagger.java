package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.core.properties.swagger.error.SwaggerJwtErrorExamples;
import io.dev.jobprep.domain.security.auth.presentation.dto.req.AuthLoginRequest;
import io.dev.jobprep.domain.security.oauth.presentation.dto.res.TokenResponse;
import io.dev.jobprep.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "테스트용 로그인 API")
@SuppressWarnings("unused")
public interface AuthSwagger {

    @Operation(summary = "로그인", description = "테스트용 Freepass 토큰을 발급받기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "E02-AUTH-001", value = SwaggerJwtErrorExamples.AUTH_MISSING_CREDENTIALS)
                ))
    })
    ResponseEntity<TokenResponse> login(
            @Valid @RequestBody AuthLoginRequest request,
            HttpServletResponse response
    );
}
