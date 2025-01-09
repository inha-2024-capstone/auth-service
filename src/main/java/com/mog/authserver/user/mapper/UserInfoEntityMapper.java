package com.mog.authserver.user.mapper;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.dto.request.UserOauthSignUpRequestDTO;
import com.mog.authserver.user.dto.request.UserSignUpRequestDTO;

public class UserInfoEntityMapper {

    public static UserInfoEntity createUserInfoEntity(UserSignUpRequestDTO userSignUpRequestDTO, AuthEntity authEntity) {
        return new UserInfoEntity(
                null,
                userSignUpRequestDTO.gender(),
                userSignUpRequestDTO.phoneNumber(),
                userSignUpRequestDTO.address(),
                userSignUpRequestDTO.nickName(),
                Constant.DEFAULT_USER_IMAGE,
                authEntity
        );
    }

    public static UserInfoEntity createUserInfoEntity(UserOauthSignUpRequestDTO userOauthSignUpRequestDTO,
                                                      UserInfoEntity userInfoEntity) {
        return new UserInfoEntity(
                userInfoEntity.getId(),
                userOauthSignUpRequestDTO.gender(),
                userOauthSignUpRequestDTO.phoneNumber(),
                userOauthSignUpRequestDTO.address(),
                userOauthSignUpRequestDTO.nickName(),
                userInfoEntity.getImageUrl(),
                userInfoEntity.getAuthEntity()
        );
    }

    public static UserInfoEntity updateNickname(UserInfoEntity userInfoEntity, String nickname) {
        return new UserInfoEntity(
                userInfoEntity.getCreateTime(),
                userInfoEntity.getModifiedTime(),
                userInfoEntity.getDeletedTime(),
                userInfoEntity.getState(),
                userInfoEntity.getId(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                nickname,
                userInfoEntity.getImageUrl(),
                userInfoEntity.getAuthEntity()
        );
    }

    public static UserInfoEntity updateImage(UserInfoEntity userInfoEntity, String imageUrl) {
        return new UserInfoEntity(
                userInfoEntity.getCreateTime(),
                userInfoEntity.getModifiedTime(),
                userInfoEntity.getDeletedTime(),
                userInfoEntity.getState(),
                userInfoEntity.getId(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                userInfoEntity.getNickName(),
                imageUrl,
                userInfoEntity.getAuthEntity()
        );
    }
}
