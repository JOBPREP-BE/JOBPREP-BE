package io.dev.jobprep.domain.users.application.dto.res;

import io.dev.jobprep.domain.users.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public final class SignUpResponse {
    private String email;
    private String username;
    private LocalDateTime time;

    public static SignUpResponse from(User user){
        return new SignUpResponse(user.getEmail(), user.getUsername(), user.getCreatedAt());
    }
}
