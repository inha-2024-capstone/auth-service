package com.mog.authserver.user.dto.response;

import com.mog.authserver.user.domain.enums.Gender;
import jakarta.validation.constraints.NotBlank;

public record UserInfoResponseDTO(
        @NotBlank
        String email,

        @NotBlank
        String username,

        Gender gender,

        String phoneNumber,

        String address,

        String imageUri,

        String nickName){
}

