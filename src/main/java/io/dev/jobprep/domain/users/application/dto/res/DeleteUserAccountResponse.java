package io.dev.jobprep.domain.users.application.dto.res;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public final class DeleteUserAccountResponse {
    private String message;

    public static DeleteUserAccountResponse from(String message){
        return new DeleteUserAccountResponse(message);
    }
}
