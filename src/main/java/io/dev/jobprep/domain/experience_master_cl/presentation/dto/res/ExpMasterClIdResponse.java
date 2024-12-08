package io.dev.jobprep.domain.experience_master_cl.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ExpMasterClIdResponse {
    @Schema(description = "자소서 Id", example ="1")
    private final Long id;

    private ExpMasterClIdResponse(Long id) {
        this.id = id;
    }

    public static ExpMasterClIdResponse from (Long id) {
        return new ExpMasterClIdResponse(id);
    }
}
