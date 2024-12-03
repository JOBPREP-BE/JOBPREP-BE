package io.dev.jobprep.domain.users.application.dto.res;

import io.dev.jobprep.domain.users.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public final class MyPageResponse {
    private String userEmail;
    private String Username;
    private UserRole userRole;
}
