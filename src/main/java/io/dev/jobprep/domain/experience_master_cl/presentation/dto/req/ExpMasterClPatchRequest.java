package io.dev.jobprep.domain.experience_master_cl.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "마스터 자소서 수정 DTO. 수정할 필드와 내용을 전달")
@NoArgsConstructor
public class ExpMasterClPatchRequest {
    @Schema(description = "수정할 내용", example ="진행 중")
    @NotBlank
    private String newVal;
}
