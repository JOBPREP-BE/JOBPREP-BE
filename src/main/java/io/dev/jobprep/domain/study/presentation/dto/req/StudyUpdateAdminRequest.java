package io.dev.jobprep.domain.study.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "스터디 필드 수정 요청 DTO. 관리자가 스터디 정보를 수정한다.")
@Getter
public class StudyUpdateAdminRequest {

    @Schema(description = "변경할 링크")
    private final String link;

    private StudyUpdateAdminRequest(String link) {
        this.link = link;
    }
}
