package io.dev.jobprep.domain.applicationstatus.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "지원 현황 생성 응답 DTO. 생성된 지원 현황 ID를 반환한다.")
@Getter

public class ApplicationStatusIdResponse {

    @Schema(description = "지원 현황 ID", example = "2", implementation = Long.class)
    @NotNull
    private final Long id;

    private ApplicationStatusIdResponse(Long id) {
        this.id = id;
    }

    public static ApplicationStatusIdResponse of(Long id) {
        return new ApplicationStatusIdResponse(id);
    }

}
