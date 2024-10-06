package com.mog.authserver.jwt.exception;

import com.mog.authserver.common.exception.BaseException;
import com.mog.authserver.common.status.enums.FailureStatus;

public class TokenNotPresentException extends BaseException {
    public TokenNotPresentException(){
        super(FailureStatus.TOKEN_NOT_PRESENT);
    }
}
