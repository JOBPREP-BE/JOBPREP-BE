package io.dev.jobprep.domain.users.application;

import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.exception.UserException;
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.dev.jobprep.exception.code.ErrorCode404.USER_NOT_FOUND;

@Service
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
        User user =userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        user.validateUserDelete();
        return user;
    }


}
