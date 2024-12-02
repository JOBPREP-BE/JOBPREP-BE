package io.dev.jobprep.users.application.dto.res;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public final class DeleteUserAccountResponse {
    private String message;
    private LocalDateTime time;

    public static DeleteUserAccountResponse from(LocalDateTime time){
        return new DeleteUserAccountResponse("회원탈퇴가 성공적으로 처리되었습니다.", time);
    }
}
