package com.mog.authserver.common.status;

import com.mog.authserver.common.response.BaseResponseBody;

public interface BaseStatus {
    <T> BaseResponseBody<T> getBaseResponseBody();

    <T> BaseResponseBody<T> getBaseResponseBody(T result);
}
