package io.dev.jobprep.domain.experience_master_cl.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "마스터 자소서 수정 응답 DTO")
@Getter
public class UpdateExpMasterClResponse {
    @Schema(description = "변경된 값", implementation = String.class)
    @NotNull
    private final String newVal;

    public static UpdateExpMasterClResponse from (String newVal) {
        return new UpdateExpMasterClResponse(newVal);
    }

    private UpdateExpMasterClResponse(String newVal) {
        this.newVal = newVal;
    }
}
