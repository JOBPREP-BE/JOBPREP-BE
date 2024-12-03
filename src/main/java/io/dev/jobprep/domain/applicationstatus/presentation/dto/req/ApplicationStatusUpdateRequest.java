package io.dev.jobprep.domain.applicationstatus.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "지원 현황 특정 레코드 수정 요청 DTO")
@Getter
@NoArgsConstructor
public class ApplicationStatusUpdateRequest {

    @Schema(description = "변경하려는 값", example = "마이크로소프트", implementation = String.class)
    @NotBlank
    private String newVal;

    @Schema(description = "값의 원본 데이터 타입", example = "String", implementation = String.class)
    @NotBlank
    private String dataType;

}
