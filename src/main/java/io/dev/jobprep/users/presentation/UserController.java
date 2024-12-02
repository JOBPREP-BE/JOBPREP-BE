package io.dev.jobprep.users.presentation;


import io.dev.jobprep.users.application.UserService;
import io.dev.jobprep.users.application.dto.req.SignUpRequest;
import io.dev.jobprep.users.application.dto.res.DeleteUserAccountResponse;
import io.dev.jobprep.users.application.dto.res.MyPageResponse;
import io.dev.jobprep.users.presentation.dto.req.SignUpAPIRequest;
import io.dev.jobprep.users.presentation.dto.res.DeleteUserAccountAPIResponse;
import io.dev.jobprep.users.presentation.dto.res.MyPageAPIResponse;
import io.dev.jobprep.users.presentation.dto.res.SignUpAPIResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value ="/signup")
    public ResponseEntity<SignUpAPIResponse> signUp(@RequestBody SignUpAPIRequest signUpRequest/*,
                                                    @AuthenticationPrincipal UserDetails userDetails*/){

        return ResponseEntity.ok(SignUpAPIResponse.from(
                userService.SignUpUser(SignUpRequest.from(signUpRequest))
                ));
    }
    @GetMapping("/mypage")
    public ResponseEntity<MyPageAPIResponse> getMyPage(@RequestParam(required = false) Long userId/*,
                                                    @AuthenticationPrincipal UserDetails userDetails*/) {
        MyPageResponse myPageResponse = userService.getUserMyPageInfo(userId);
        return ResponseEntity.ok(MyPageAPIResponse.from(myPageResponse));
    }

    @DeleteMapping("/")
    public ResponseEntity<DeleteUserAccountAPIResponse> deleteMyAccount(@RequestParam(required = false) Long userId/*,
                                                    @AuthenticationPrincipal UserDetails userDetails*/){
        DeleteUserAccountResponse response = userService.deleteUserAcount(userId);
        return ResponseEntity.ok(DeleteUserAccountAPIResponse.from(response));

    }

}
