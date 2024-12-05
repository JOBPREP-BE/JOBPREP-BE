package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.core.properties.swagger.error.SwaggerStudyErrorExamples;
import io.dev.jobprep.core.properties.swagger.error.SwaggerUserErrorExamples;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyCreateRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyUpdateAdminRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyUpdateRequest;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyCommonResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyIdResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyInfoAdminResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyInfoResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyUpdateAdminResponse;
import io.dev.jobprep.domain.study.presentation.dto.res.StudyUpdateResponse;
import io.dev.jobprep.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Study", description = "스터디 관련 API")
@SuppressWarnings("unused")
public interface StudySwagger {

    @Operation(summary = "스터디 생성", description = "사용자가 스터디를 생성할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "스터디 생성 성공",
            content = @Content(schema = @Schema(implementation = StudyIdResponse.class))),
        @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E00-STUDY-001", value = SwaggerStudyErrorExamples.DUPLICATE_STUDY_NAME),
                    @ExampleObject(name = "E01-STUDY-001", value = SwaggerStudyErrorExamples.ALREADY_CREATED_STUDY)
                }
            )),
        @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            )),
    })
    ResponseEntity<StudyIdResponse> create(
        @Parameter(description = "유저 ID", required = true) Long userId,
        StudyCreateRequest req
    );

    @Operation(summary = "스터디 참여", description = "사용자가 스터디에 참여할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "스터디 참여 성공",
            content = @Content(schema = @Schema(implementation = StudyIdResponse.class))),
        @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples =  {
                    @ExampleObject(name = "E01-STUDY-001", value = SwaggerStudyErrorExamples.INVALID_STUDY_STATUS_TO_RECRUIT),
                    @ExampleObject(name = "E01-STUDY-003", value = SwaggerStudyErrorExamples.ALREADY_CREATED_STUDY),
                    @ExampleObject(name = "E01-STUDY-004", value = SwaggerStudyErrorExamples.ALREADY_FINISHED_STUDY),
                    @ExampleObject(name = "E01-STUDY-005", value = SwaggerStudyErrorExamples.ALREADY_DELETED_STUDY),
                    @ExampleObject(name = "E01-STUDY-006", value = SwaggerStudyErrorExamples.STUDY_GATHERED_USER_EXCEED),
                    @ExampleObject(name = "E01-STUDY-007", value = SwaggerStudyErrorExamples.ALREADY_GATHERED_STUDY),
                    @ExampleObject(name = "E01-STUDY-008", value = SwaggerStudyErrorExamples.ALREADY_PASSED_DUE_DATE)
                }
            )),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E03-STUDY-001", value = SwaggerStudyErrorExamples.STUDY_NOT_FOUND),
                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            }))
    })
    ResponseEntity<StudyIdResponse> join(
        @Parameter(description = "유저 ID", required = true) Long userId,
        @PathVariable Long id
    );

    @Operation(summary = "스터디 삭제", description = "관리자가 스터디를 삭제할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "스터디 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E01-STUDY-005", value = SwaggerStudyErrorExamples.ALREADY_DELETED_STUDY)
            )),
        @ApiResponse(responseCode = "403", description = "스터디 삭제 권한이 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E02-STUDY-002", value = SwaggerUserErrorExamples.ADMIN_FORBIDDEN_OPERATION)
            )),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E03-STUDY-001", value = SwaggerStudyErrorExamples.STUDY_NOT_FOUND),
                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                }
            ))
    })
    ResponseEntity<Void> delete(
        @Parameter(description = "유저 ID", required = true) Long userId,
        @PathVariable Long id
    );

    @Operation(summary = "모든 스터디 조회", description = "관리자가 스터디를 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "스터디 조회 성공"),
        @ApiResponse(responseCode = "403", description = "스터디 조회 권한이 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E02-STUDY-002", value = SwaggerUserErrorExamples.ADMIN_FORBIDDEN_OPERATION)
            )),
        @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            )),
    })
    ResponseEntity<List<StudyInfoAdminResponse>> getAllForAdmin(
        @Parameter(description = "유저 ID", required = true) Long userId
    );

    @Operation(summary = "스터디 수정", description = "관리자가 스터디를 수정할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "스터디 수정 성공",
            content = @Content(schema = @Schema(implementation = StudyUpdateAdminResponse.class))),
        @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E01-STUDY-004", value = SwaggerStudyErrorExamples.ALREADY_FINISHED_STUDY),
                    @ExampleObject(name = "E01-STUDY-005", value = SwaggerStudyErrorExamples.ALREADY_DELETED_STUDY)
                }
            )),
        @ApiResponse(responseCode = "403", description = "스터디 수정 권한이 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E02-STUDY-002", value = SwaggerUserErrorExamples.ADMIN_FORBIDDEN_OPERATION)
            )),
        @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            )),
    })
    ResponseEntity<StudyUpdateAdminResponse> modifyForAdmin(
        @Parameter(description = "유저 ID", required = true) Long userId,
        @PathVariable Long id, @PathVariable String field,
        StudyUpdateAdminRequest request
    );

    @Operation(summary = "스터디 수정", description = "스터디 참여자가 스터디를 수정할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "스터디 수정 성공",
            content = @Content(schema = @Schema(implementation = StudyUpdateResponse.class))),
        @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E01-STUDY-004", value = SwaggerStudyErrorExamples.ALREADY_FINISHED_STUDY),
                    @ExampleObject(name = "E01-STUDY-005", value = SwaggerStudyErrorExamples.ALREADY_DELETED_STUDY)
                }
            )),
        @ApiResponse(responseCode = "403", description = "스터디를 수정할 권한이 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E02-STUDY-002", value = SwaggerStudyErrorExamples.STUDY_FORBIDDEN_OPERATION)
            )),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E03-STUDY-001", value = SwaggerStudyErrorExamples.STUDY_NOT_FOUND),
                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                }
            ))
    })
    ResponseEntity<StudyUpdateResponse> modify(
        @Parameter(description = "유저 ID", required = true) Long userId,
        @PathVariable Long id,
        StudyUpdateRequest request
    );

    @Operation(summary = "모집중인 스터디 조회", description = "사용자가 모집 중인 스터디를 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "스터디 조회 성공"),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            ))
    })
    ResponseEntity<List<StudyCommonResponse>> getRecruitingStudy(Long userId);

    @Operation(summary = "참여 중인 스터디 조회", description = "사용자가 자신이 참여 중인 스터디를 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "스터디 조회 성공",
            content = @Content(schema = @Schema(implementation = StudyInfoResponse.class))),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            ))
    })
    ResponseEntity<StudyInfoResponse> getMyStudy(
        @Parameter(description = "유저 ID", required = true) Long userId
    );
}
