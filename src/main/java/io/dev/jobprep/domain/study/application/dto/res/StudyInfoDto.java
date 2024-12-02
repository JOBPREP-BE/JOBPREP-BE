package io.dev.jobprep.domain.study.application.dto.res;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.StudySchedule;
import io.dev.jobprep.domain.study.domain.entity.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "모집 중인 스터디 정보 응답 DTO.")
@Getter
public class StudyInfoDto {

    private static final int SECONDS = 3600;

    private final Long id;

    private final String name;

    private final LocalDateTime startDate;

    private final int headCount;

    private final Position position;

    private final LocalDateTime dueDate;

    @Builder
    private StudyInfoDto(
        Long id,
        String name,
        LocalDateTime startDate,
        int headCount,
        Position position,
        LocalDateTime dueDate
    ) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.headCount = headCount;
        this.position = position;
        this.dueDate = dueDate;
    }

    public static StudyInfoDto of(Study study, StudySchedule studySchedule, int headCount) {
        return StudyInfoDto.builder()
            .id(study.getId())
            .name(study.getName())
            .startDate(studySchedule.getStart_date())
            .headCount(headCount)
            .position(study.getPosition())
            .dueDate(calculateDueDate(studySchedule.getStart_date()))
            .build();
    }

    private static LocalDateTime calculateDueDate(LocalDateTime startDate) {
        return startDate.minusSeconds(SECONDS);
    }

}
