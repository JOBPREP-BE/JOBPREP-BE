package io.dev.jobprep.domain.users.presentation.dto.res;

import io.dev.jobprep.domain.users.application.dto.res.MyPageResponse;
import io.dev.jobprep.domain.users.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Schema(description = "마이페이지 정보 응답. 유저의 이메일, 유저네임, 권한을 반납")
public final class MyPageAPIResponse {

    @Schema(description = "유저 이메일", example ="seocd@seocd.com", implementation = String.class)
    private String userEmail;
    @Schema(description = "유저네임", example ="오리덕덕", implementation = String.class)
    private String username;
    @Schema(description = "유저권한", example ="NORMAL", implementation = String.class)
    private UserRole userRole;


    public static MyPageAPIResponse from(MyPageResponse myPageResponse){
        return new MyPageAPIResponse(myPageResponse.getUserEmail(),
                myPageResponse.getUsername(),
                myPageResponse.getUserRole()
        );
    }
}
