package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.core.properties.swagger.error.SwaggerJwtErrorExamples;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
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
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @ApiResponse(responseCode = "400", description = "리프레쉬토큰 캐싱에 실패했을 때",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E01-JWT-001", value = SwaggerJwtErrorExamples.RFT_CACHE_FALIURE))),
            @ApiResponse(responseCode = "400", description = "리프레쉬토큰 캐시 검증에서 문제가 발생했을 때",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E01-JWT-003", value = SwaggerJwtErrorExamples.RFT_VALIDATION_FAILURE))),

    })
    ResponseEntity<TokenResponse> reissue(
            @Parameter(description = "갱신용 리프레시 토큰", required = true)
            @RequestHeader(value = "XRefreshToken") String refreshToken,
            @Parameter(description = "HTTP 응답 객체", required = true)
                    HttpServletResponse response);

    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 처리하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
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
            @ApiResponse(responseCode = "400", description = "리프레쉬토큰 캐시 삭제에 실패했을 때",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E01-JWT-002", value = SwaggerJwtErrorExamples.RFT_CACHE_DELETION_FALIURE))),
    })
    ResponseEntity<Void> logout(
            @Parameter(description = "인증된 사용자 정보", required = true)
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "HTTP 응답 객체", required = true)
            HttpServletResponse response);
}