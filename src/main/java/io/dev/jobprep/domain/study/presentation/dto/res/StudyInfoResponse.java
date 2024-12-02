package io.dev.jobprep.domain.study.presentation.dto.res;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.StudySchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "참여 중인 스터디 조회 응답. 스터디의 상세 정보를 반환한다.")
@Getter
public class StudyInfoResponse {

    @Schema(description = "스터디 ID", example = "2")
    private final Long id;

    @Schema(description = "스터디 스케줄 정보")
    private final List<StudyScheduleInfoResponse> startDates;

    @Schema(description = "구글 DOCS 링크")
    private final String googleLink;

    @Schema(description = "디스코드 링크")
    private final String discordLink;

    @Schema(description = "오픈 카카오톡 링크")
    private final String kakaoLink;

    @Builder
    private StudyInfoResponse(
        Long id,
        List<StudyScheduleInfoResponse> startDates,
        String googleLink,
        String discordLink,
        String kakaoLink
    ) {
        this.id = id;
        this.startDates = startDates;
        this.googleLink = googleLink;
        this.discordLink = discordLink;
        this.kakaoLink = kakaoLink;
    }

    public static StudyInfoResponse of(Study study, List<StudySchedule> schedules) {

        List<StudyScheduleInfoResponse> startDates = schedules.stream()
            .map(StudyScheduleInfoResponse::of)
            .toList();

        return StudyInfoResponse.builder()
            .id(study.getId())
            .startDates(startDates)
            .googleLink(study.getGoogle_link())
            .discordLink(study.getDiscord_link())
            .kakaoLink(study.getKakao_link())
            .build();
    }
}
