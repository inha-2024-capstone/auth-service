package com.mog.authserver.user.mapper;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.dto.request.OauthSignUpRequestDTO;
import com.mog.authserver.user.dto.request.SignUpRequestDTO;

public class UserInfoEntityMapper {

    public static UserInfoEntity toUserInfoEntity(SignUpRequestDTO signUpRequestDTO) {
        return new UserInfoEntity(
                signUpRequestDTO.email(),
                signUpRequestDTO.username(),
                signUpRequestDTO.password(),
                signUpRequestDTO.role(),
                signUpRequestDTO.gender(),
                signUpRequestDTO.phoneNumber(),
                signUpRequestDTO.address(),
                signUpRequestDTO.nickName(),
                null,
                signUpRequestDTO.loginSource()
        );
    }

    public static UserInfoEntity updateUserInfoEntity(UserInfoEntity userInfoEntity,
                                                      OauthSignUpRequestDTO oauthSignUpRequestDTO) {
        return new UserInfoEntity(
                userInfoEntity.getId(),
                userInfoEntity.getCreateTime(),
                userInfoEntity.getModifiedTime(),
                userInfoEntity.getDeletedTime(),
                userInfoEntity.getState(),
                userInfoEntity.getEmail(),
                userInfoEntity.getUsername(),
                userInfoEntity.getPassword(),
                userInfoEntity.getRole(),
                oauthSignUpRequestDTO.gender(),
                oauthSignUpRequestDTO.phoneNumber(),
                oauthSignUpRequestDTO.address(),
                oauthSignUpRequestDTO.nickName(),
                oauthSignUpRequestDTO.imageUri(),
                userInfoEntity.getLoginSource()
        );
    }

    public static UserInfoEntity updatePassword(UserInfoEntity userInfoEntity, String encodedPassword) {
        return new UserInfoEntity(
                userInfoEntity.getId(),
                userInfoEntity.getCreateTime(),
                userInfoEntity.getModifiedTime(),
                userInfoEntity.getDeletedTime(),
                userInfoEntity.getState(),
                userInfoEntity.getEmail(),
                userInfoEntity.getUsername(),
                encodedPassword,
                userInfoEntity.getRole(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                userInfoEntity.getNickName(),
                userInfoEntity.getImageUrl(),
                userInfoEntity.getLoginSource()
        );
    }

    public static UserInfoEntity updateNickname(UserInfoEntity userInfoEntity, String nickname) {
        return new UserInfoEntity(
                userInfoEntity.getId(),
                userInfoEntity.getCreateTime(),
                userInfoEntity.getModifiedTime(),
                userInfoEntity.getDeletedTime(),
                userInfoEntity.getState(),
                userInfoEntity.getEmail(),
                userInfoEntity.getUsername(),
                userInfoEntity.getPassword(),
                userInfoEntity.getRole(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                nickname,
                userInfoEntity.getImageUrl(),
                userInfoEntity.getLoginSource()
        );
    }

    public static UserInfoEntity updateImageUrl(UserInfoEntity userInfoEntity, String imageUrl) {
        return new UserInfoEntity(
                userInfoEntity.getId(),
                userInfoEntity.getCreateTime(),
                userInfoEntity.getModifiedTime(),
                userInfoEntity.getDeletedTime(),
                userInfoEntity.getState(),
                userInfoEntity.getEmail(),
                userInfoEntity.getUsername(),
                userInfoEntity.getPassword(),
                userInfoEntity.getRole(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                userInfoEntity.getNickName(),
                imageUrl,
                userInfoEntity.getLoginSource()
        );
    }
}
