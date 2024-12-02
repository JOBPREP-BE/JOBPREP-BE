package io.dev.jobprep.users.application.dto.req;


import io.dev.jobprep.users.presentation.dto.req.SignUpAPIRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class SignUpRequest {
    private String email;
    private String username;
    public static SignUpRequest from(SignUpAPIRequest signUpAPIRequest){
        return new SignUpRequest(signUpAPIRequest.getEmail(), signUpAPIRequest.getUsername());
    }
}
