package com.mog.authserver.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * BaseResponseBody 는 성공 시 result가 존재하고, 실패 시 null 값이 들어감.
 * @param <T> 전달해야하는 데이터 타입
 */
@AllArgsConstructor
@Getter
public class BaseResponseBody<T> {
    private final Boolean isSucceeded;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;
}