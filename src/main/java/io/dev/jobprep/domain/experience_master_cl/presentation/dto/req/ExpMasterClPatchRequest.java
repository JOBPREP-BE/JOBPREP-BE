package io.dev.jobprep.domain.experience_master_cl.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "마스터 자소서 수정 DTO. 수정할 필드와 내용을 전달")
public class ExpMasterClPatchRequest {
    @Schema(description = "수정할 필드", example ="expAnalProcess")
    private final String field;
    @Schema(description = "수정할 내용", example ="진행 중")
    private final String content;

    private ExpMasterClPatchRequest(String field, String content) {
        this.field = field;
        this.content = content;
    }
}
