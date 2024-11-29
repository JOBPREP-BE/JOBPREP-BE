package io.dev.jobprep.users.application.dto.res;

import io.dev.jobprep.users.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyPageResponse {
    private String userEmail;
    private String Username;
    private UserRole userRole;
}
