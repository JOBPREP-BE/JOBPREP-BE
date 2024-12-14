package io.dev.jobprep.common.swagger.template;

import io.dev.jobprep.common.base.CursorPaginationReq;
import io.dev.jobprep.common.base.CursorPaginationResult;
import io.dev.jobprep.core.properties.swagger.error.SwaggerChatErrorExamples;
import io.dev.jobprep.core.properties.swagger.error.SwaggerUserErrorExamples;
import io.dev.jobprep.domain.chat.presentation.dto.res.ChatMessageCommonResponse;
import io.dev.jobprep.domain.chat.presentation.dto.res.ChatRoomAdminResponse;
import io.dev.jobprep.domain.chat.presentation.dto.res.ChatRoomIdResponse;
import io.dev.jobprep.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

public interface ChatSwagger {

    @Operation(summary = "채팅방 생성", description = "사용자가 첫 메시지를 보내기 전에 채팅방 생성을 위해 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "채팅방 생성 성공",
            content = @Content(schema = @Schema(implementation = ChatRoomIdResponse.class))),
        @ApiResponse(responseCode = "400", description = "요청한 데이터가 유효하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E01-CHATROOM-003", value = SwaggerChatErrorExamples.CHAT_ROOM_ALREADY_EXIST)
            )),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            ))
    })
    ResponseEntity<ChatRoomIdResponse> create(@Parameter(required = true) Long userId);

    @Operation(summary = "채팅 메시지 내역 조회", description = "사용자가 관리자와의 채팅 메시지 내역을 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "채팅 메시지 내역 조회 성공"),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            ))
    })
    ResponseEntity<CursorPaginationResult<ChatMessageCommonResponse>> getMyMessageHistory(
        @Parameter(required = true) Long userId,
        @Valid @ModelAttribute CursorPaginationReq pageable
    );

    @Operation(summary = "채팅 메시지 내역 조회", description = "관리자가 유저와의 채팅 메시지 내역을 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "채팅 메시지 내역 조회 성공"),
        @ApiResponse(responseCode = "403", description = "메시지 내역을 조회할 권한이 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E02-CHATROOM-001", value = SwaggerChatErrorExamples.CHAT_ROOM_FORBIDDEN_OPERATION)
            )),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND),
                    @ExampleObject(name = "E03-CHATROOM-001", value = SwaggerChatErrorExamples.CHATROOM_NOT_FOUND)
                }
            ))
    })
    ResponseEntity<CursorPaginationResult<ChatMessageCommonResponse>> getUsersMessageHistoryForAdmin(
        @Parameter(required = true) Long userId,
        @PathVariable String id,
        @Valid @ModelAttribute CursorPaginationReq pageable
    );

    @Operation(summary = "활성화된 채팅방 리스트 조회", description = "관리자가 활성화된 채팅방 목록을 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "채팅방 리스트 조회 성공"),
        @ApiResponse(responseCode = "403", description = "메시지 내역을 조회할 권한이 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E02-CHATROOM-001", value = SwaggerChatErrorExamples.CHAT_ROOM_FORBIDDEN_OPERATION)
            )),
        @ApiResponse(responseCode = "404", description = "요청한 데이터가 존재하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(name = "E03-USER-001", value = SwaggerUserErrorExamples.USER_NOT_FOUND)
            ))
    })
    ResponseEntity<List<ChatRoomAdminResponse>> getActiveChatRoomsForAdmin(
        @Parameter(required = true) Long userId
    );

}
