package io.dev.jobprep.users.application;
import io.dev.jobprep.exception.exception_class.UserException;
import io.dev.jobprep.users.application.dto.req.SignUpRequest;
import io.dev.jobprep.users.application.dto.res.DeleteUserAccountResponse;
import io.dev.jobprep.users.application.dto.res.MyPageResponse;
import io.dev.jobprep.users.application.dto.res.SignUpResponse;
import io.dev.jobprep.users.domain.User;
import io.dev.jobprep.users.infrastructure.UserRepository;
import io.dev.jobprep.users.utility.UserValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static io.dev.jobprep.exception.code.ErrorCode400.USER_ACCOUNT_ALREADY_EXISTS;
import static io.dev.jobprep.exception.code.ErrorCode404.USER_NOT_FOUND;

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
        Optional<User> userData = userRepository.findUserByEmail(signUpRequest.getEmail());

        if (userData.isPresent()){
            User existingUser = userData.get();

            try {//탈퇴했던 회원인지 체크
                UserValidator.validateUserActive(existingUser);
            }catch(UserException e){//탈퇴취소 로직
                existingUser.unSetDeletedAt();
            }
        }else{
            throw new UserException(USER_ACCOUNT_ALREADY_EXISTS);
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
    //유저 찾는 로직
    public MyPageResponse getUserMyPageInfo(Long userId){
        User userData = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        UserValidator.validateUserActive(userData);

        return MyPageResponse.builder().userEmail(userData.getEmail())
                .Username(userData.getUsername())
                .userRole(userData.getUserRole())
                .build();

    }

    //유저 삭제 로직
    public DeleteUserAccountResponse deleteUserAcount(Long userId){
        User userData = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        //이미 탈퇴처리 된 유저 처리
        UserValidator.validateUserActive(userData);

        LocalDateTime timeNow = userData.setDeletedAt();

        return DeleteUserAccountResponse.builder()
                .time(timeNow)
                .Message("회원탈퇴가 성공적으로 처리되었습니다.")
                .build();
    }

}
