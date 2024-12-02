package io.dev.jobprep.domain.study.presentation.dto.req;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.StudySchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "스터디 스케줄 생성 요청")
public class StudyScheduleCreateRequest {

    private final int INIT_WEEK_NUMBER = 1;

    @Schema(description = "스터디 ID", example = "3", implementation = Long.class)
    @NotNull
    private final Long studyId;

    @Schema(description = "시작 일자", example = "2024-02-18T:14:00:00", pattern = "yyyy-MM-dd'T'HH:mm:ss", type = "string")
    @NotNull
    private final LocalDateTime startDate;

    @Builder
    private StudyScheduleCreateRequest(Long studyId, LocalDateTime startDate) {
        this.studyId = studyId;
        this.startDate = startDate;
    }

    public StudySchedule toEntity(Study study, LocalDateTime startDate) {
        return StudySchedule.builder()
            .study(study)
            .start_date(startDate)
            .week_number(INIT_WEEK_NUMBER)
            .build();
    }

    public StudySchedule toInitEntity(Study study, int week_number) {
        return StudySchedule.builder()
            .study(study)
            .week_number(week_number)
            .build();
    }
}
