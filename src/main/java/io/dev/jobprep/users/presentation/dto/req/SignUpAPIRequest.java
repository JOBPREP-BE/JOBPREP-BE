package io.dev.jobprep.users.presentation.dto.req;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class SignUpAPIRequest {
    private String email;
    private String username;

}
