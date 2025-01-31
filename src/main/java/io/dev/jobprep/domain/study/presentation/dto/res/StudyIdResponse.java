package io.dev.jobprep.domain.study.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "스터디 생성 응답. 생성된 스터디의 ID를 반환한다.")
@Getter
public class StudyIdResponse {

    @Schema(description = "생성된 스터디 ID", example = "1")
    private final Long id;

    private StudyIdResponse(Long id) {
        this.id = id;
    }

    public static StudyIdResponse from(Long id) {
        return new StudyIdResponse(id);
    }

}
