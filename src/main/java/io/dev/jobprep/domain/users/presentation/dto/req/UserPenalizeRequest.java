package io.dev.jobprep.domain.users.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "유저 페널티 부여 요청 DTO. 관리자가 유저에게 페널티를 부여할 때 사용한다.")
@Getter
@NoArgsConstructor
public class UserPenalizeRequest {

    @Schema(description = "페널티를 부여할 유저 ID", example = "2", implementation = Long.class)
    @NotNull
    private Long userId;

    @Schema(description = "유저가 탈주한 스터디 이름", example = "3주안에 박살내는 kotlin 정복기", implementation = String.class)
    @NotBlank
    private String name;

}
