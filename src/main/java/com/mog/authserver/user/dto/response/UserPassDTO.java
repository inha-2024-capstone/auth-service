package com.mog.authserver.user.dto.response;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Gender;
import jakarta.validation.constraints.NotBlank;

public record UserPassDTO(@NotBlank
                          String email,

                          @NotBlank
                          String username,

                          Gender gender,

                          String phoneNumber,

                          String address,

                          String imageUri,

                          String nickName) {

    public static UserPassDTO from(UserInfoEntity userInfoEntity) {
        return new UserPassDTO(
                userInfoEntity.getAuthEntity().getEmail(),
                userInfoEntity.getAuthEntity().getUsername(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                userInfoEntity.getImageUrl(),
                userInfoEntity.getNickName()
        );
    }
}
