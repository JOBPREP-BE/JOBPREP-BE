package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.core.properties.swagger.error.SwaggerJobInterviewErrorExamples;
import io.dev.jobprep.core.properties.swagger.error.SwaggerUserErrorExamples;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.FindJobInterviewResponse;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.JobInterviewIdResponse;
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

import java.util.List;

@Tag(name = "Job Interview", description = "면접 관련 API")
@SuppressWarnings("unused")
public interface JobInterviewSwagger {

    @Operation(summary = "면접 생성", description = "사용자가 면접을 생성할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "면접 생성 성공"),
            @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                    )),
    })
    ResponseEntity<JobInterviewIdResponse> save();

    @Operation(summary = "면접 삭제", description = "면접 데이터를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "면접 삭제 성공",
                    content = @Content(schema = @Schema(implementation = JobInterviewIdResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E01-STUDY-005", value = SwaggerJobInterviewErrorExamples.ALREADY_DELETED_INTERVIEW)
                    )),
            @ApiResponse(responseCode = "403", description = "스터디 삭제 권한이 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-STUDY-002", value = SwaggerJobInterviewErrorExamples.INTERVIEW_FORBIDDEN_OPERATION)
                    )),
            @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "E03-STUDY-001", value = SwaggerJobInterviewErrorExamples.INTERVIEW_NOT_FOUND),
                                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                            }
                    ))
    })
    ResponseEntity<Void> delete (@PathVariable("interviewId") Long interviewId);

    @Operation(summary = "면접 조회", description = "모든 면접 데이터를 조회합니다.")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "면접 조회 성공"),
            @ApiResponse(responseCode = "403", description = "스터디 조회 권한이 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-STUDY-002", value = SwaggerJobInterviewErrorExamples.INTERVIEW_FORBIDDEN_OPERATION)
                    )),
            @ApiResponse(responseCode = "404", description = "요청한 사용자가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                    )),
    })
    ResponseEntity<List<FindJobInterviewResponse>> find ();

    @Operation(summary = "면접 수정", description = "면접 데이터를 수정합니다.")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "면접 수정 성공",
                    content = @Content(schema = @Schema(implementation = FindJobInterviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "E01-STUDY-005", value = SwaggerJobInterviewErrorExamples.ALREADY_DELETED_INTERVIEW)
                            }
                    )),
            @ApiResponse(responseCode = "403", description = "스터디를 수정할 권한이 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E02-STUDY-002", value = SwaggerJobInterviewErrorExamples.INTERVIEW_FORBIDDEN_OPERATION)
                    )),
            @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "E03-STUDY-001", value = SwaggerJobInterviewErrorExamples.INTERVIEW_NOT_FOUND),
                                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
                            }
                    ))
    })
    ResponseEntity<FindJobInterviewResponse> update (
            @PathVariable("interviewId") Long id, @RequestBody PutJobInterviewRequest dto
    );
}
