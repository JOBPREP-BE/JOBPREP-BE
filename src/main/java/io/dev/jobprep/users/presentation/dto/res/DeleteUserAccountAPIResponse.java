package io.dev.jobprep.users.presentation.dto.res;

import io.dev.jobprep.users.application.dto.res.DeleteUserAccountResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DeleteUserAccountAPIResponse {
    private String Message;
    private LocalDateTime time;
    public static DeleteUserAccountAPIResponse from(DeleteUserAccountResponse response){
        return new DeleteUserAccountAPIResponse(response.getMessage(), response.getTime());
    }
}
