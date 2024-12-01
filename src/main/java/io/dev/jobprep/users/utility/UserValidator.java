package io.dev.jobprep.users.utility;


import io.dev.jobprep.exception.exception_class.UserException;
import io.dev.jobprep.users.domain.User;

import static io.dev.jobprep.exception.code.ErrorCode400.USER_ACCOUNT_WITHDRAWN_ERROR;

public class UserValidator {
    public static void validateUserActive(User user) {
        if (user.getDeletedAt() != null) throw new UserException(USER_ACCOUNT_WITHDRAWN_ERROR);
    }
}