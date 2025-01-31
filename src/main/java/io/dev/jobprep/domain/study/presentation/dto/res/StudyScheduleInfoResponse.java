package io.dev.jobprep.domain.study.presentation.dto.res;

import io.dev.jobprep.domain.study.domain.entity.StudySchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "스터디 스케줄 정보 응답 DTO")
@Getter
public class StudyScheduleInfoResponse {

    @Schema(description = "스터디 스케줄 ID", example = "1")
    private final Long scheduleId;

    @Schema(description = "시작일자", example = "2024-02-18T:14:00:00")
    private final LocalDateTime startDate;

    @Schema(description = "스터디 주차", example = "2")
    private final int weekNumber;

    @Builder
    private StudyScheduleInfoResponse(Long scheduleId, LocalDateTime startDate, int weekNumber) {
        this.scheduleId = scheduleId;
        this.startDate = startDate;
        this.weekNumber = weekNumber;
    }

    public static StudyScheduleInfoResponse of(StudySchedule studySchedule) {
        return StudyScheduleInfoResponse.builder()
            .scheduleId(studySchedule.getId())
            .startDate(studySchedule.getStart_date())
            .weekNumber(studySchedule.getWeek_number())
            .build();
    }

}
