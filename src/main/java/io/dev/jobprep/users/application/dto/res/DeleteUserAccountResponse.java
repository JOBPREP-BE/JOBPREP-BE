package io.dev.jobprep.users.application.dto.res;

import io.dev.jobprep.users.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class DeleteUserAccountResponse {
    private String Message;
    private LocalDateTime time;
}
