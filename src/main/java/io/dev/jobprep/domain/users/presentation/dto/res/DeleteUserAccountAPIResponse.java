package io.dev.jobprep.domain.users.presentation.dto.res;

import io.dev.jobprep.domain.users.application.dto.res.DeleteUserAccountResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public final class DeleteUserAccountAPIResponse {
    private String Message;
    public static DeleteUserAccountAPIResponse from(DeleteUserAccountResponse response){
        return new DeleteUserAccountAPIResponse(response.getMessage());
    }
}
