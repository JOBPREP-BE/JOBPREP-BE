package io.dev.jobprep.domain.security.auth.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "임시로 사용하는 자체 로그 요청 DTO")
@Getter
@NoArgsConstructor
public class AuthLoginRequest {

    @Schema(description = "아이디", example = "jobprep12321@gmail.com", implementation = String.class)
    @NotBlank
    private String id;

    @Schema(description = "비밀번호", example = "password!123", implementation = String.class)
    @NotBlank
    private String password;

}
