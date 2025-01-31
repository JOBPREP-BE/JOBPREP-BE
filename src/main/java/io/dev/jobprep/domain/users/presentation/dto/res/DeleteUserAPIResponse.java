package io.dev.jobprep.domain.users.presentation.dto.res;

import io.dev.jobprep.domain.users.application.dto.res.DeleteUserAccountResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "회원 탈퇴에 대한 응답 DTO. 회원 탈퇴 메시지를 출력한다")
public final class DeleteUserAPIResponse {

    @Schema(description = "탈퇴 메세지", example = "회원탈퇴가 성공적으로 처리되었습니다.", implementation = String.class)
    private String message;
    public static DeleteUserAPIResponse from(DeleteUserAccountResponse response){
        return new DeleteUserAPIResponse(response.getMessage());
    }
}
