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
            .id((Long) resolve(dto, Long.class))
            .name((String) resolve(dto, String.class))
            .startDate((LocalDateTime) resolve(dto, LocalDateTime.class))
            .headCount(headCount)
            .position((Position) resolve(dto, Position.class))
            .dueDate(resolve(dto))
            .build();
    }

    private static Object resolve(StudyWithStartDateDto dto, Class<?> clazz) {
        if (clazz == String.class) {
            return dto.getStudy() != null ? dto.getStudy().getName() : null;
        } else if (clazz == Long.class) {
            return dto.getStudy() != null ? dto.getStudy().getId() : null;
        } else if (clazz == LocalDateTime.class) {
            return dto.getStartDate() != null ? dto.getStartDate() : null;
        } else if (clazz == Position.class) {
            return dto.getStudy() != null ? dto.getStudy().getPosition() : null;
        } else {
            throw new IllegalArgumentException("Unknown class type: " + clazz);
        }
    }

    private static LocalDateTime resolve(StudyWithStartDateDto dto) {
        return dto.getStartDate() != null ? calculateDueDate(dto.getStartDate()) : null;
    }

    private static LocalDateTime calculateDueDate(LocalDateTime startDate) {
        return startDate.minusSeconds(SECONDS);
    }

}
