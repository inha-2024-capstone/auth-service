package com.mog.authserver.common.status;

import com.mog.authserver.common.response.BaseResponseBody;
import org.springframework.http.ResponseEntity;

public interface  BaseStatus {
    <T> ResponseEntity<BaseResponseBody<T>> getResponseBody(T result);

    <T> ResponseEntity<BaseResponseBody<T>> getResponseBody();
}
