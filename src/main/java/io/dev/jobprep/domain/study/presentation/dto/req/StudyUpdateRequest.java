package io.dev.jobprep.domain.study.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "스터디 스케줄 변경 요청 DTO. 참여자가 스터디 정보를 변경한다.")
@Getter
public class StudyUpdateRequest {

    @Schema(description = "시작일자", example = "2024-02-18T:14:00:00")
    private final LocalDateTime startDate;

    @Schema(description = "스터디 주차", example = "2")
    private final int weekNumber;

    @Builder
    private StudyUpdateRequest(LocalDateTime startDate, int weekNumber) {
        this.startDate = startDate;
        this.weekNumber = weekNumber;
    }

}
