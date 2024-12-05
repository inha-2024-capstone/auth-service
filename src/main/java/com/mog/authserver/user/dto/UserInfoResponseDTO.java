package com.mog.authserver.user.dto;

import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInfoResponseDTO(
        @NotBlank
        String email,

        @NotBlank
        String username,

        Role role,

        Gender gender,

        String phoneNumber,

        String address,

        String nickName,

        @NotNull
        LoginSource loginSource){
}

