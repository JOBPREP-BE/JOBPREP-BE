package io.dev.jobprep.domain.users.presentation.dto.res;

import io.dev.jobprep.domain.users.application.dto.res.SignUpResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@Schema(description = "회원가입 요청 DTO, 유저의 기본적인 정보를 포함")
public final class SignUpAPIResponse {


    @Schema(description = "회원가입 완료 메세지", example ="회원가입이 완료됐습니다")
    private String message;
    @Schema(description = "유저 이메일", example ="seocd@seocd.com")
    private String email;
    @Schema(description = "유저네임", example ="오리덕덕")
    private String username;
    @Schema(description = "가입 시간", example ="2024-12-24T:14:32:11")
    private LocalDateTime time;

    public static SignUpAPIResponse from(SignUpResponse signUpResponse){
        String message = "회원가입이 완료됐습니다";
        return new SignUpAPIResponse(
                message,
                signUpResponse.getEmail(),
                signUpResponse.getUsername(),
                signUpResponse.getTime());
    }
}
