package io.dev.jobprep.domain.chat.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "채팅 메시지 전송 요청 DTO")
@Getter
@NoArgsConstructor
public class ChatMessageRequest {

    @Schema(description = "유저 ID", example = "2", implementation = Long.class)
    @NotNull
    private Long userId;

    @Schema(description = "메시지", example = "문의드릴 게 있어서 메시지 남깁니다.")
    @NotBlank
    private String message;

}
