package com.mog.authserver.common.status.enums;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.BaseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@Getter
public enum FailureStatus implements BaseStatus {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "잘못된 요청입니다."),
    USER_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "USER4000", "주어진 이메일의 유저가 존재합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4040", "유저가 존재하지 않습니다."),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "TOKEN4000", "토큰이 유효하지 않습니다."),
    TOKEN_NOT_PRESENT(HttpStatus.BAD_REQUEST, "TOKEN4040", "Authorization header가 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER5000", "서버 내부에 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public <T> ResponseEntity<BaseResponseBody<T>> getResponseBody(T result) {
        return ResponseEntity
                .status(this.httpStatus)
                .body(new BaseResponseBody<>(false, this.code, this.message, result));
    }

    @Override
    public <T> ResponseEntity<BaseResponseBody<T>> getResponseBody() {
        return ResponseEntity
                .status(this.httpStatus)
                .build();
    }
}
