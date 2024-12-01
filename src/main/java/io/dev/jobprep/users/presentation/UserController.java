package io.dev.jobprep.users.presentation;


import io.dev.jobprep.users.application.UserService;
import io.dev.jobprep.users.application.dto.res.DeleteUserAccountResponse;
import io.dev.jobprep.users.application.dto.res.MyPageResponse;
import io.dev.jobprep.users.presentation.dto.res.DeleteUserAccountAPIResponse;
import io.dev.jobprep.users.presentation.dto.res.MyPageAPIResponse;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mypage")
    public ResponseEntity<MyPageAPIResponse> getMyPage(@RequestParam(required = false) Long userId/*,
                                                    @AuthenticationPrincipal UserDetails userDetails*/) {
        MyPageResponse myPageResponse = userService.getUserMyPageInfo(userId);
        return ResponseEntity.ok(MyPageAPIResponse.from(myPageResponse));
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<DeleteUserAccountAPIResponse> deleteMyAccount(@RequestParam(required = false) Long userId/*,
                                                    @AuthenticationPrincipal UserDetails userDetails*/){
        DeleteUserAccountResponse response = userService.deleteUserAcount(userId);
        return ResponseEntity.ok(DeleteUserAccountAPIResponse.from(response));

    }

}
