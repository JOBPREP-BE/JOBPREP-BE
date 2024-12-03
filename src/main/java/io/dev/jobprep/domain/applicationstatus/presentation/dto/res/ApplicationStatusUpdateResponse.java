package io.dev.jobprep.domain.applicationstatus.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "지원 현황 특정 레코드 수정 응답 DTO. 변경한 값을 반환한다.")
@Getter
public class ApplicationStatusUpdateResponse {

    @Schema(description = "변경된 값", example = "마이크로소프트", implementation = String.class)
    @NotNull
    private final String newVal;

    private ApplicationStatusUpdateResponse(String newVal) {
        this.newVal = newVal;
    }

    public static ApplicationStatusUpdateResponse from(String newVal) {
        return new ApplicationStatusUpdateResponse(newVal);
    }
}
