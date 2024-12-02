package io.dev.jobprep.users.application.dto.res;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public final class DeleteUserAccountResponse {
    private String Message;
    private LocalDateTime time;
}
