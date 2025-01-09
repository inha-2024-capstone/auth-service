package com.mog.authserver.user.dto.request;

import com.mog.authserver.user.domain.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserOauthSignUpRequestDTO(
        @NotNull(message = "성별을 입력해야 합니다.")
        Gender gender,

        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
        String phoneNumber,

        @NotBlank(message = "주소지를 입력해야 합니다.")
        String address,

        @NotBlank
        String nickName) {
}
