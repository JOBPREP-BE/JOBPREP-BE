package io.dev.jobprep.domain.study.presentation.dto.res;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "어드민 스터디 리스트 조회 응답, 관리자가 스터디 리스트를 조회한다.")
@Getter
public class StudyInfoAdminResponse {

    @Schema(description = "스터디 ID", example = "2")
    private final Long roomId;

    @Schema(description = "스터디 이름", example = "프로젝트가 서쪽으로 간 까닭은")
    private final String name;

    @Schema(description = "구글 DOCS 링크")
    private final String googleLink;

    @Schema(description = "디스코드 링크")
    private final String discordLink;

    @Schema(description = "오픈 카카오톡 링크")
    private final String kakaoLink;

    @Builder
    private StudyInfoAdminResponse(
        Long roomId,
        String name,
        String googleLink,
        String discordLink,
        String kakaoLink
    ) {
        this.roomId = roomId;
        this.name = name;
        this.googleLink = googleLink;
        this.discordLink = discordLink;
        this.kakaoLink = kakaoLink;
    }

    public static StudyInfoAdminResponse of(Study study) {
        return StudyInfoAdminResponse.builder()
            .roomId(study.getId())
            .name(study.getName())
            .googleLink(study.getGoogle_link())
            .discordLink(study.getDiscord_link())
            .kakaoLink(study.getKakao_link())
            .build();
    }

}
