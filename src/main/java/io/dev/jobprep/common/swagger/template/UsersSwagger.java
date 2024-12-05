package io.dev.jobprep.common.swagger.template;


import io.dev.jobprep.core.properties.swagger.error.SwaggerStudyErrorExamples;
import io.dev.jobprep.core.properties.swagger.error.SwaggerUserErrorExamples;
import io.dev.jobprep.domain.users.presentation.dto.req.SignUpAPIRequest;
import io.dev.jobprep.domain.users.presentation.dto.res.DeleteUserAPIResponse;
import io.dev.jobprep.domain.users.presentation.dto.res.MyPageAPIResponse;
import io.dev.jobprep.domain.users.presentation.dto.res.SignUpAPIResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name="Users", description = "유저 관련 API")

public interface UsersSwagger {

    @Operation(summary="회원가입", description = "사용자가 회원가입할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description="유저 생성 성공",
                    content = @Content(schema=@Schema(implementation = SignUpAPIResponse.class))),
            @ApiResponse(responseCode = "400", description = "해당 이메일로 가입한 유저가 이미 존재",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E01-STUDY-001", value = SwaggerUserErrorExamples.USER_ACCOUNT_ALREADY_EXISTS)
                    ))
    })
    ResponseEntity<SignUpAPIResponse> signUp(@RequestBody SignUpAPIRequest signUpRequest/*,
                                                    @AuthenticationPrincipal UserDetails userDetails*/);
    @Operation(summary = "마이페이지 조회", description = "사용자가 자신의 마이페이지 정보를 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이페이지 조회 성공",
                    content = @Content(schema = @Schema(implementation = MyPageAPIResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                    ))

    })
    ResponseEntity<MyPageAPIResponse> getMyPage(@RequestParam(required = false) Long userId);

    @Operation(summary = "계정 삭제", description = "사용자가 자신의 계정을 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계정 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteUserAPIResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                    ))
    })
    ResponseEntity<DeleteUserAPIResponse> deleteMyAccount(@RequestParam(required = false) Long userId);
}