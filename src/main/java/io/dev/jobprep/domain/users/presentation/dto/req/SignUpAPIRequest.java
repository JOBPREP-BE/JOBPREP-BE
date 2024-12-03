package io.dev.jobprep.domain.users.presentation.dto.req;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public final class SignUpAPIRequest {
    private String email;
    private String username;

}
