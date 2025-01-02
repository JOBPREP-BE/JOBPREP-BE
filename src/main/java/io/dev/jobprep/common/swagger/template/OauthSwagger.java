package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.core.properties.swagger.error.SwaggerJwtErrorExamples;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.oauth.presentation.dto.TokenResponse;
import io.dev.jobprep.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "OAuth", description = "OAuth로그인용 path 조회")
public interface OauthSwagger {

    @Operation(summary = "토큰 재발급", description = "Refresh 토큰을 사용하여 새로운 Access 토큰을 발급받는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenInfo.class),
                            examples = @ExampleObject(value =  """
                            {
                              "grantType": "Bearer",
                              "authorization": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIiwiYXV0aCI6Ik5PUk1BTCIsImVtYWlsIjoic2VvY2QxMjM0NTZAbmF2ZXIuY29tIiwiaWF0IjoxNzM0MTkxNDk4LCJleHAiOjE3MzQyOTk0OTh9.PYvvHL3Ito6r-ls_8Rn1s4exhEt1TEPoBGzpXoF0ENA",
                              "xRefreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIiwiZXhwIjoxNzM2NzgzNDk4fQ.EcWwUnuJ2_Csfb5MEX1isTI_v3P5SCLKvkUt_-zbLtg"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "토큰이 만료되었을 떄",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-AUTH-003", value = SwaggerJwtErrorExamples.AUTH_TOKEN_EXPIRED))),
            @ApiResponse(responseCode = "401", description = "인증 정보가 올바르지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-AUTH-001", value = SwaggerJwtErrorExamples.AUTH_MISSING_CREDENTIALS))),
            @ApiResponse(responseCode = "403", description = "유저가 권한에 맞지 않는 요청을 할 때",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-AUTH-002", value = SwaggerJwtErrorExamples.AUTH_ACCESS_DENIED))),
    })
    ResponseEntity<TokenResponse> reissueRefreshToken(
            @Parameter(description = "갱신용 리프레시 토큰", required = true)
            @RequestHeader(value = "XRefreshToken") String refreshToken);
}