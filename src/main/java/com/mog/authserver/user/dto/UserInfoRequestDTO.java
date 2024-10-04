package com.mog.authserver.user.dto;

import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserInfoRequestDTO(
        @NotBlank(message = "이메일을 입력해야 합니다.")
        String email,
        @NotBlank(message = "사용자 이름을 입력해야 합니다.")
        String username,
        @Size(min=10, max=15)
        @NotBlank(message = "비밀번호를 입력해야 합니다.")
        String password,
        @NotNull(message = "역할을 부여해야 합니다.")
        Role role,
        @NotNull(message = "성별을 입력해야 합니다.")
        Gender gender,
        @Size(min=13, max=13) @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
        String phoneNumber,
        @NotBlank(message = "주소지를 입력해야 합니다.")
        String address,
        @NotBlank(message = "닉네임을 입력해야 합니다.")
        String nickName,
        @NotNull(message = "로그인 소스를 부여해야 합니다.")
        LoginSource loginSource
) { }
