package io.dev.jobprep.domain.users.presentation.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@Schema(description = "회원가입 요청 DTO. 이메일과 유저네임을 전달한다.")
public final class SignUpAPIRequest {
    @Schema(description = "유저 이메일", example ="seocd@seocd.com")
    private String email;
    @Schema(description = "유저네임", example ="오리덕덕")
    private String username;

}
