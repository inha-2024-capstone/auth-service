package com.mog.authserver.user.exception.handler;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.FailureStatus;
import com.mog.authserver.user.exception.UserAlreadyExistException;
import com.mog.authserver.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserInfoExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseResponseBody<String>> userNotFoundExHandler(UserNotFoundException userNotFoundException) {
        return FailureStatus.USER_NOT_FOUND.getResponseBody(userNotFoundException.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<BaseResponseBody<String>> userAlreadyExistExHandler(
            UserAlreadyExistException userAlreadyExistException) {
        return FailureStatus.USER_ALREADY_EXISTED.getResponseBody(userAlreadyExistException.getMessage());
    }
}