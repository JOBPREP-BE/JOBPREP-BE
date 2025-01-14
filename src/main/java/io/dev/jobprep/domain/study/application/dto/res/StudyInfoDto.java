package io.dev.jobprep.domain.study.application.dto.res;

import io.dev.jobprep.domain.study.domain.entity.enums.Position;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

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

    public static StudyInfoDto of(StudyWithStartDateDto dto, int headCount) {
        return StudyInfoDto.builder()
            .id(dto.getStudy().getId())
            .name(dto.getStudy().getName())
            .startDate(dto.getStartDate())
            .headCount(headCount)
            .position(dto.getStudy().getPosition())
            .dueDate(calculateDueDate(dto.getStartDate()))
            .build();
    }

    private static LocalDateTime calculateDueDate(LocalDateTime startDate) {
        return startDate.minusSeconds(SECONDS);
    }

}
