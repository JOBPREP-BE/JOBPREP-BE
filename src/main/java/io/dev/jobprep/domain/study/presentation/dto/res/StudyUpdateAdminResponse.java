package io.dev.jobprep.domain.study.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "스터디 필드 수정 응답 DTO. 변경된 값을 반환한다.")
@Getter
public class StudyUpdateAdminResponse {

    @Schema(description = "변경된 링크")
    private final String link;

    private StudyUpdateAdminResponse(String link) {
        this.link = link;
    }

    public static StudyUpdateAdminResponse of(String link) {
        return new StudyUpdateAdminResponse(link);
    }
}
