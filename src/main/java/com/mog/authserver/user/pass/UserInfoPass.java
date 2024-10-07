package com.mog.authserver.user.pass;

import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInfoPass(
        @NotBlank
        String email,
        @NotBlank
        String username,
        @NotNull
        Role role,
        @NotNull
        Gender gender,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String address,
        @NotBlank
        String nickName,
        @NotBlank
        String imageUrl,
        @NotNull
        LoginSource loginSource
) {
}
