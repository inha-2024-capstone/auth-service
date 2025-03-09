package com.mog.authserver.common.exception;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.FailureStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseBody<String>> userExHandler(Exception exception) {
        log.info(exception.getMessage());
        return FailureStatus.INTERNAL_SERVER_ERROR.getResponseBody(exception.getMessage());
    }
}
