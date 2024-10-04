package com.mog.authserver.user.exception;

import com.mog.authserver.common.exception.BaseException;
import com.mog.authserver.common.status.enums.FailureStatus;

public class UserAlreadyExistException extends BaseException {

    public UserAlreadyExistException(){
        super(FailureStatus.USER_ALREADY_EXISTED);
    }
}
