package com.mog.authserver.common.status.enums;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.BaseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@Getter
public enum SuccessStatus implements BaseStatus {

    OK(HttpStatus.OK, "COMMON2000", "성공입니다."),
    CREATED(HttpStatus.CREATED, "COMMON2010", "생성되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public <T> ResponseEntity<BaseResponseBody<T>> getResponseBody(T result) {
        return ResponseEntity
                .status(this.getHttpStatus())
                .body(new BaseResponseBody<>(true, this.code, this.message, result));
    }

    @Override
    public <T> ResponseEntity<BaseResponseBody<T>> getResponseBody() {
        return ResponseEntity
                .status(this.getHttpStatus())
                .build();
    }
}
