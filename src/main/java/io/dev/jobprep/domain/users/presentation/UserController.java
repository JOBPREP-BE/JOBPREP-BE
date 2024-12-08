package io.dev.jobprep.domain.users.presentation;


import io.dev.jobprep.common.swagger.template.UsersSwagger;
import io.dev.jobprep.domain.users.application.dto.res.SignUpResponse;
import io.dev.jobprep.domain.users.presentation.dto.res.SignUpAPIResponse;
import io.dev.jobprep.domain.users.application.UserService;
import io.dev.jobprep.domain.users.application.dto.req.SignUpRequest;
import io.dev.jobprep.domain.users.application.dto.res.DeleteUserAccountResponse;
import io.dev.jobprep.domain.users.application.dto.res.MyPageResponse;
import io.dev.jobprep.domain.users.presentation.dto.req.SignUpAPIRequest;
import io.dev.jobprep.domain.users.presentation.dto.res.DeleteUserAPIResponse;
import io.dev.jobprep.domain.users.presentation.dto.res.MyPageAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UsersSwagger {
    private final UserService userService;

    @PostMapping(value ="/signup")
    public ResponseEntity<SignUpAPIResponse> signUp(@RequestBody SignUpAPIRequest signUpRequest/*,
                                                    @AuthenticationPrincipal UserDetails userDetails*/){
        SignUpResponse newUser = userService.SignUpUser(SignUpRequest.from(signUpRequest));
        URI location = URI.create("/mypage?"+ newUser.getId().toString());
        return ResponseEntity.created(location).body(SignUpAPIResponse.from(newUser));
    }
    @GetMapping("/mypage")
    public ResponseEntity<MyPageAPIResponse> getMyPage(@RequestParam(required = false) Long userId/*,
                                                    @AuthenticationPrincipal UserDetails userDetails*/) {
        MyPageResponse myPageResponse = userService.getUserMyPageInfo(userId);
        return ResponseEntity.ok(MyPageAPIResponse.from(myPageResponse));
    }

    @DeleteMapping("/")
    public ResponseEntity<DeleteUserAPIResponse> deleteMyAccount(@RequestParam(required = false) Long userId/*,
                                                    @AuthenticationPrincipal UserDetails userDetails*/){
        DeleteUserAccountResponse response = userService.deleteUser(userId);
        return ResponseEntity.ok(DeleteUserAPIResponse.from(response));

    }

}
