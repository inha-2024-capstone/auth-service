package com.mog.authserver.user.exception;

import com.mog.authserver.common.exception.BaseException;
import com.mog.authserver.common.status.enums.FailureStatus;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message) {
        super(FailureStatus.USER_NOT_FOUND, message);
    }
}
