package io.dev.jobprep.users.application.dto.res;

import io.dev.jobprep.users.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SignUpResponse {
    private String email;
    private String username;
    private LocalDateTime time;

    public static SignUpResponse from(User user){
        return new SignUpResponse(user.getEmail(), user.getUsername(), user.getCreatedAt());
    }
}
