package io.dev.jobprep.domain.study.presentation.dto.req;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "스터디 생성 요청")
@NoArgsConstructor
public class StudyCreateRequest {

    @Schema(description = "스터디를 생성하는 유저 ID", example = "2", implementation = Long.class)
    @NotNull
    private Long creatorId;

    @Schema(description = "스터디 이름", example = "3주안에 박살내는 kotlin 정복기", implementation = String.class)
    @NotBlank
    private String name;

    @Schema(description = "스터디 직무", example = "개발", implementation = String.class)
    @NotNull
    private Position position;

    @Schema(description = "시작 일자", example = "2024-02-18T:14:00:00", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull
    private LocalDateTime startDate;

    @Schema(description = "구글 DOCS 링크", example = "", implementation = String.class)
    private String googleLink;

    @Schema(description = "디스코드 링크", example = "", implementation = String.class)
    private String discordLink;

    @Schema(description = "오픈 카카오톡 링크", example = "", implementation = String.class)
    private String kakaoLink;

    public Study toEntity() {
        return Study.builder()
            .creatorId(creatorId)
            .name(name)
            .position(position)
            .google_link(googleLink)
            .discord_link(discordLink)
            .kakao_link(kakaoLink)
            .build();
    }

    public StudyScheduleCreateRequest from(Long studyId) {
        return StudyScheduleCreateRequest.builder()
            .studyId(studyId)
            .startDate(startDate)
            .build();
    }
}
