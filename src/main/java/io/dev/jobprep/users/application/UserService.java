package io.dev.jobprep.users.application;
import io.dev.jobprep.users.application.dto.req.SignUpRequest;
import io.dev.jobprep.users.application.dto.res.DeleteUserAccountResponse;
import io.dev.jobprep.users.application.dto.res.MyPageResponse;
import io.dev.jobprep.users.application.dto.res.SignUpResponse;
import io.dev.jobprep.users.domain.User;
import io.dev.jobprep.users.infrastructure.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public SignUpResponse SignUpUser(SignUpRequest signUpRequest) {
        // 이메일 중복 확인
        //TODO 탈퇴 유저 재가입시 로직 필요
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 중복되지 않을 경우 새 사용자 저장
        User newUser = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .build();

        //TODO error exception 처리
        return SignUpResponse.from(userRepository.save(newUser));
    }
    //TODO 나중에 JWT 관련 로직으로 처리하기
    public MyPageResponse getUserMyPageInfo(Long userId){
        User userData = userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return MyPageResponse.builder().userEmail(userData.getEmail())
                .Username(userData.getUsername())
                .userRole(userData.getUserRole())
                .build();

    }

    public DeleteUserAccountResponse deleteUserAcount(Long userId){
        User userData = userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        LocalDateTime timeNow = userData.setDeletedAt();
        userRepository.save(userData);

        return DeleteUserAccountResponse.builder()
                .time(timeNow)
                .Message("회원탈퇴가 성공적으로 처리되었습니다.")
                .build();
    }

}
