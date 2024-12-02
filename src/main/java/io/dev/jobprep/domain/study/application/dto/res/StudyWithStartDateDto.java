package io.dev.jobprep.domain.study.application.dto.res;

import io.dev.jobprep.domain.study.domain.entity.Study;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StudyWithStartDateDto {

    private final Study study;

    private final LocalDateTime startDate;

    @Builder
    private StudyWithStartDateDto(Study study, LocalDateTime startDate) {
        this.study = study;
        this.startDate = startDate;
    }

    public static StudyWithStartDateDto of(Study study, LocalDateTime startDate) {
        return StudyWithStartDateDto.builder()
            .study(study)
            .startDate(startDate)
            .build();
    }

}
