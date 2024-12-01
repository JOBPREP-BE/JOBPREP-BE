package io.dev.jobprep.users.application;
import io.dev.jobprep.users.application.dto.res.DeleteUserAccountResponse;
import io.dev.jobprep.users.application.dto.res.MyPageResponse;
import io.dev.jobprep.users.domain.User;
import io.dev.jobprep.users.infrastructure.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
