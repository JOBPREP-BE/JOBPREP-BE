package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.common.constants.TokenHeaderConstants;
import io.dev.jobprep.core.properties.swagger.error.SwaggerJwtErrorExamples;
import io.dev.jobprep.core.properties.swagger.error.SwaggerTempTokenErrorExamples;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.oauth.presentation.dto.res.TokenResponse;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "OAuth", description = "OAuth로그인용 path 조회")
public interface OauthSwagger {

    @Operation(summary = "OAuth 임시 토큰 교환", description = "OAuth 로그인 후 발급된 임시 토큰을 JWT 토큰으로 교환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 교환 성공",
                    headers = {
                            @Header(name = "Set-Cookie",
                                    description = "리프레쉬 토큰 쿠키",
                                    schema = @Schema(type= "string", example = "XRefreshToken=eyJhbGciOiJIUzI1NiIs...; " +
                                            "Path=/; " +
                                            "HttpOnly; " +
                                            "Secure; " +
                                            "Max-Age=1209600; " +
                                            "SameSite=Lax"))

                    },
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "임시 토큰 검증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E01-TMPT-001", value = SwaggerTempTokenErrorExamples.TMPT_VALIDATION_FAILURE))),
            @ApiResponse(responseCode = "400", description = "임시 토큰 캐싱 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E01-TMPT-003", value = SwaggerTempTokenErrorExamples.TMPT_CACHE_FALIURE))),
            @ApiResponse(responseCode = "400", description = "리프레시 토큰 캐싱 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E01-JWT-001", value = SwaggerJwtErrorExamples.RFT_CACHE_FALIURE)))

    })
    ResponseEntity<TokenResponse> callBack(
            @Parameter(description = "OAuth 로그인 후 발급된 임시 토큰", required = true)
            @RequestParam String otpToken,
            @Parameter(description = "HTTP 요청 객체", required = true)
            HttpServletRequest request,
            @Parameter(description = "HTTP 응답 객체", required = true)
            HttpServletResponse response
    );


    @Operation(summary = "토큰 재발급", description = "Refresh 토큰을 사용하여 새로운 Access 토큰을 발급받는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    headers = {
                            @Header(name = "Set-Cookie",
                                    description = "리프레쉬 토큰 쿠키",
                                    schema = @Schema(type= "string", example = "XRefreshToken=eyJhbGciOiJIUzI1NiIs...; " +
                                            "Path=/; " +
                                            "HttpOnly; " +
                                            "Secure; " +
                                            "Max-Age=1209600; " +
                                            "SameSite=Lax"))

                    },
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
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
            @CookieValue(value = TokenHeaderConstants.REFRESH_HEADER) String refreshToken,
            @Parameter(description = "CSRF 토큰", required = true)
            @RequestHeader(value = TokenHeaderConstants.CSRF_HEADER) String csrfToken,
            @Parameter(description = "HTTP 요청 객체", required = true)
            HttpServletRequest request,
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
    })
    ResponseEntity<Void> logout(
            @Parameter(description = "인증된 사용자 정보", required = true)
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "HTTP 요청 객체", required = true)
            HttpServletRequest request,
            @Parameter(description = "HTTP 응답 객체", required = true)
            HttpServletResponse response);
}