package io.dev.jobprep.domain.study.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "스터디 필드 수정 응답 DTO. 변경된 값을 반환한다.")
@Getter
public class StudyUpdateResponse {

    @Schema(description = "시작일자", example = "2024-02-18T:14:00:00")
    private final LocalDateTime startDate;

    @Schema(description = "스터디 주차", example = "2")
    private final int weekNumber;

    @Builder
    private StudyUpdateResponse(LocalDateTime startDate, int weekNumber) {
        this.startDate = startDate;
        this.weekNumber = weekNumber;
    }

    public static StudyUpdateResponse of(LocalDateTime startDate, int weekNumber) {
        return StudyUpdateResponse.builder()
            .startDate(startDate)
            .weekNumber(weekNumber)
            .build();
    }

}
