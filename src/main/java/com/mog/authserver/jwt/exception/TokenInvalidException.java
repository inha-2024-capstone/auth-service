package com.mog.authserver.jwt.exception;

import com.mog.authserver.common.exception.BaseException;
import com.mog.authserver.common.status.enums.FailureStatus;

public class TokenInvalidException extends BaseException {

    public TokenInvalidException(){
        super(FailureStatus.TOKEN_INVALID);
    }
}
