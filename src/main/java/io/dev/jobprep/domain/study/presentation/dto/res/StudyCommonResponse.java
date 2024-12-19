package io.dev.jobprep.domain.study.presentation.dto.res;

import io.dev.jobprep.domain.study.application.dto.res.StudyInfoDto;
import io.dev.jobprep.domain.study.domain.entity.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "모집 중인 스터디 리스트 정보 응답. 스터디의 공통 정보를 반환한다.")
@Getter
public class StudyCommonResponse {

    @Schema(description = "스터디 ID", example = "2")
    private final Long roomId;

    @Schema(description = "스터디 이름", example = "3주만에 박살내는 kotlin 정복기")
    private final String name;

    @Schema(description = "스터디 시작 일자", example = "2024-02-18T:14:00:00")
    private final LocalDateTime startDate;

    @Schema(description = "모집된 인원", example = "1")
    private final int headCount;

    @Schema(description = "직무", example = "PROGRAMMING")
    private final Position position;

    @Schema(description = "스터디 모집 마감 일자", example = "2024-02-28T:14:00:00")
    private final LocalDateTime dueDate;

    @Builder
    private StudyCommonResponse(
        Long roomId,
        String name,
        LocalDateTime startDate,
        int headCount,
        Position position,
        LocalDateTime dueDate
    ) {
        this.roomId = roomId;
        this.name = name;
        this.startDate = startDate;
        this.headCount = headCount;
        this.position = position;
        this.dueDate = dueDate;
    }

    public static StudyCommonResponse from(StudyInfoDto dto) {
        return StudyCommonResponse.builder()
            .roomId(dto.getId())
            .name(dto.getName())
            .startDate(dto.getStartDate())
            .headCount(dto.getHeadCount())
            .position(dto.getPosition())
            .dueDate(dto.getDueDate())
            .build();
    }
}
