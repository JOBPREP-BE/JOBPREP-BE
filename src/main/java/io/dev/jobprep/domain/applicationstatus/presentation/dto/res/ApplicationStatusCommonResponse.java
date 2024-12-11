package io.dev.jobprep.domain.applicationstatus.presentation.dto.res;

import io.dev.jobprep.domain.applicationstatus.domain.entity.ApplicationStatus;
import io.dev.jobprep.domain.applicationstatus.domain.entity.enums.ApplicationProcess;
import io.dev.jobprep.domain.applicationstatus.domain.entity.enums.ApplicationProgress;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "지원 현황 리스트 조회 응답 DTO")
@Getter
public class ApplicationStatusCommonResponse {

    @Schema(description = "지원 현황 ID", example = "2", implementation = Long.class)
    @Nullable
    private final Long id;

    @Schema(description = "지원 기업", example = "애플", implementation = String.class)
    @Nullable
    private final String company;

    @Schema(description = "지원 직무", example = "SW 개발", implementation = String.class)
    @Nullable
    private final String position;

    @Schema(description = "진행 상황", example = "진행 전", implementation = String.class)
    @Nullable
    private final ApplicationProgress progress;

    @Schema(description = "전형 단계", example = "서류 전형", implementation = String.class)
    @Nullable
    private final ApplicationProcess process;

    @Schema(description = "지원 일자", example = "2024-12-04T12:00:00", implementation = LocalDateTime.class)
    @Nullable
    private final LocalDateTime applicationDate;

    @Schema(description = "마감 일자", example = "2024-12-25T16:00:00", implementation = LocalDateTime.class)
    @Nullable
    private final LocalDateTime dueDate;

    @Schema(description = "바로가기 링크", example = "", implementation = String.class)
    @Nullable
    private final String url;

    @Schema(description = "자기소개서", example = "저를 꼭 뽑아주세요!", implementation = String.class)
    @Nullable
    private final String coverLetter;

    @Builder
    private ApplicationStatusCommonResponse(
        Long id,
        String company,
        String position,
        ApplicationProgress progress,
        ApplicationProcess process,
        LocalDateTime applicationDate,
        LocalDateTime dueDate,
        String url,
        String coverLetter
    ) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.progress = progress;
        this.process = process;
        this.applicationDate = applicationDate;
        this.dueDate = dueDate;
        this.url = url;
        this.coverLetter = coverLetter;
    }

    public static ApplicationStatusCommonResponse from(ApplicationStatus applicationStatus) {
        return ApplicationStatusCommonResponse.builder()
            .id(applicationStatus.getId())
            .company(applicationStatus.getCompany())
            .position(applicationStatus.getPosition())
            .progress(applicationStatus.getProgress())
            .process(applicationStatus.getProcess())
            .applicationDate(applicationStatus.getApplicationDate())
            .dueDate(applicationStatus.getDueDate())
            .url(applicationStatus.getUrl())
            .coverLetter(applicationStatus.getCoverLetter())
            .build();
    }

}
