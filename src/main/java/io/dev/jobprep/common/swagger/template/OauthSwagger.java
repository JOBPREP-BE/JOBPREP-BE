package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.core.properties.swagger.error.SwaggerJwtErrorExamples;
import io.dev.jobprep.core.properties.swagger.error.SwaggerStudyErrorExamples;
import io.dev.jobprep.core.properties.swagger.error.SwaggerUserErrorExamples;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyIdResponse;
import io.dev.jobprep.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Tag(name = "OAuth", description = "" +
        "[소셜 로그인 진행 과정]\n" +
        "1. GET /api/v1/oauth2/login-urls 를 호출하여 소셜 로그인 URL을 얻습니다.\n" +
        "2. 얻은 URL로 리다이렉트하면 해당 소셜 로그인 페이지로 이동합니다.\n" +
        "3. 소셜 로그인 성공 시 자동으로 토큰이 발급되어 클라이언트로 전달됩니다.\n" +
        "4. 발급된 토큰으로 서비스 API를 호출할 수 있습니다.")
public interface OauthSwagger {

    @Operation(summary = "OAuth 로그인 URL 조회", description = "각 소셜 로그인(Google, Kakao, Naver)에 대한 인증 URL을 반환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OAuth URL 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = """
                                            {
                                                "google": "/api/v1/oauth2/authorize/google",
                                                "kakao": "/api/v1/oauth2/authorize/kakao",
                                                "naver": "/api/v1/oauth2/authorize/naver"
                                            }
                                            """
                            )))
    })
    ResponseEntity<Map<String, String>> getOAuthUrls();

    @Operation(summary = "토큰 재발급", description = "Refresh 토큰을 사용하여 새로운 Access 토큰을 발급받는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(schema = @Schema(type = "object")),
                    headers = {
                            @Header(name = "Authorization",
                                    description = "Bearer {new_access_token}",
                                    schema = @Schema(type = "string")),
                            @Header(name = "X-Refresh-Token",
                                    description = "{new_refresh_token}",
                                    schema = @Schema(type = "string"))
                    }),
            @ApiResponse(responseCode = "401", description = "토큰이 만료되었을 떄",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-AUTH-002", value = SwaggerJwtErrorExamples.AUTH_TOKEN_EXPIRED))),
            @ApiResponse(responseCode = "401", description = "인증 정보가 올바르지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-AUTH-001", value = SwaggerJwtErrorExamples.AUTH_MISSING_CREDENTIALS))),
            @ApiResponse(responseCode = "403", description = "유저가 권한에 맞지 않는 요청을 할 때",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-AUTH-001", value = SwaggerJwtErrorExamples.AUTH_ACCESS_DENIED))),
    })
    ResponseEntity<Void> refreshToken(
            @Parameter(description = "갱신용 리프레시 토큰", required = true)
            @RequestHeader(value = "x-refresh-token") String refreshToken);
}