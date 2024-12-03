package io.dev.jobprep.domain.users.presentation.dto.res;

import io.dev.jobprep.domain.users.application.dto.res.MyPageResponse;
import io.dev.jobprep.domain.users.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class MyPageAPIResponse {
    private String userEmail;
    private String Username;
    private UserRole userRole;


    public static MyPageAPIResponse from(MyPageResponse myPageResponse){
        return new MyPageAPIResponse(myPageResponse.getUserEmail(),
                myPageResponse.getUsername(),
                myPageResponse.getUserRole()
        );
    }
}
