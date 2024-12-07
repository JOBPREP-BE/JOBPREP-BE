package io.dev.jobprep.domain.users.application;

import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.domain.UserRole;
import io.dev.jobprep.domain.users.exception.UserException;
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.dev.jobprep.exception.code.ErrorCode404.ADMIN_NOT_FOUND;
import static io.dev.jobprep.exception.code.ErrorCode404.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class UserCommonService {
    private final UserRepository userRepository;
    @Autowired
    public UserCommonService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserWithId(Long id){
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        user.validateUserActive();
        return user;
    }


    public User getUserWithEmail(String email){
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        user.validateUserDelete();
        return user;
    }

    public User getUserWithRole(UserRole role) {
        User user = userRepository.findUserByRole(role)
            .orElseThrow(() -> new UserException(ADMIN_NOT_FOUND));
        user.validateUserActive();
        user.validateAdmin();
        return user;
    }


}
