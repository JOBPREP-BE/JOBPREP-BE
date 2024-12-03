package io.dev.jobprep.domain.applicationstatus.presentation.dto.req;

import io.dev.jobprep.domain.applicationstatus.domain.entity.ApplicationStatus;
import io.dev.jobprep.domain.applicationstatus.domain.entity.enums.ApplicationProcess;
import io.dev.jobprep.domain.applicationstatus.domain.entity.enums.ApplicationProgress;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "지원현황 생성 요청 Dto")
@Getter
@NoArgsConstructor
public class ApplicationStatusCreateRequest {

    @Schema(description = "지원 기업", example = "애플", implementation = String.class)
    @Nullable
    private String company;

    @Schema(description = "지원 직무", example = "SW 개발", implementation = String.class)
    @Nullable
    private String position;

    @Schema(description = "진행 상황", example = "진행 전", implementation = String.class)
    @Nullable
    private String progress;

    @Schema(description = "전형 단계", example = "서류 전형", implementation = String.class)
    @Nullable
    private String process;

    @Schema(description = "지원 일자", example = "2024-12-04T12:00:00", implementation = LocalDateTime.class)
    @Nullable
    private LocalDateTime applicationDate;

    @Schema(description = "마감 일자", example = "2024-12-25T16:00:00", implementation = LocalDateTime.class)
    @Nullable
    private LocalDateTime dueDate;

    @Schema(description = "바로가기 링크", example = "", implementation = String.class)
    @Nullable
    private String url;

    public ApplicationStatus toEntity(Long userId) {
        return ApplicationStatus.builder()
            .userId(userId)
            .company(company)
            .position(position)
            .progress(ApplicationProgress.from(progress))
            .process(ApplicationProcess.from(process))
            .applicationDate(applicationDate)
            .dueDate(dueDate)
            .url(url)
            .build();
    }

}
