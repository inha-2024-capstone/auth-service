package com.mog.authserver.user.mapper;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.dto.request.OauthSignUpRequestDTO;
import com.mog.authserver.user.dto.request.SignUpRequestDTO;

public class UserInfoEntityMapper {

    public static UserInfoEntity toUserInfoEntity(SignUpRequestDTO userInfoSignUpRequestDTO){
        return new UserInfoEntity(
                userInfoSignUpRequestDTO.email(),
                userInfoSignUpRequestDTO.username(),
                userInfoSignUpRequestDTO.password(),
                userInfoSignUpRequestDTO.role(),
                userInfoSignUpRequestDTO.gender(),
                userInfoSignUpRequestDTO.phoneNumber(),
                userInfoSignUpRequestDTO.address(),
                userInfoSignUpRequestDTO.nickName(),
                null,
                userInfoSignUpRequestDTO.loginSource()
        );
    }

    public static UserInfoEntity updateUserInfoEntity(UserInfoEntity userInfoEntity, OauthSignUpRequestDTO oauthSignUpRequestDTO){
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

    public static UserInfoEntity updatePassword(UserInfoEntity userInfoEntity, String encodedPassword){
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
}
