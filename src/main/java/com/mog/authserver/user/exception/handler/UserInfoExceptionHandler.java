package com.mog.authserver.user.exception.handler;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.FailureStatus;
import com.mog.authserver.user.exception.UserAlreadyExistException;
import com.mog.authserver.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserInfoExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseResponseBody<?>> userNotFoundExHandler(UserNotFoundException userNotFoundException){
        log.info(userNotFoundException.getMessage());
        return ResponseEntity.status(FailureStatus.USER_NOT_FOUND.getHttpStatus())
                .body(FailureStatus.USER_NOT_FOUND.getBaseResponseBody());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<BaseResponseBody<?>> userAlreadyExistExHandler(UserAlreadyExistException userAlreadyExistException){
        log.info(userAlreadyExistException.getMessage());
        return ResponseEntity.status(FailureStatus.USER_ALREADY_EXISTED.getHttpStatus())
                .body(FailureStatus.USER_ALREADY_EXISTED.getBaseResponseBody());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseBody<?>> userExHandler(Exception exception){
        log.info(exception.getMessage());
        return ResponseEntity.status(FailureStatus.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(FailureStatus.INTERNAL_SERVER_ERROR.getBaseResponseBody());
    }
}