package io.dev.jobprep.domain.study.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "스터디 스케줄 변경 요청 DTO. 참여자가 스터디 정보를 변경한다.")
@Getter
@NoArgsConstructor
public class StudyUpdateRequest {

    @Schema(description = "시작일자", example = "2024-02-18T:14:00:00")
    @NotNull
    private LocalDateTime startDate;

    @Schema(description = "스터디 주차", example = "2")
    private int weekNumber;

    @Builder
    private StudyUpdateRequest(LocalDateTime startDate, int weekNumber) {
        this.startDate = startDate;
        this.weekNumber = weekNumber;
    }

}
