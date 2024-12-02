package io.dev.jobprep.users.presentation.dto.res;

import io.dev.jobprep.users.application.dto.res.SignUpResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public final class SignUpAPIResponse {

    private String message;
    private String email;
    private String username;
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
