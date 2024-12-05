package com.mog.authserver.user.mapper;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.dto.request.OauthSignUpRequestDTO;
import com.mog.authserver.user.dto.response.UserInfoResponseDTO;
import com.mog.authserver.user.dto.request.SignUpRequestDTO;
import com.mog.authserver.user.pass.UserInfoPass;

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

    public static UserInfoResponseDTO toUserInfoResponseDTO(UserInfoEntity userInfoEntity){
        return new UserInfoResponseDTO(
                userInfoEntity.getEmail(),
                userInfoEntity.getUsername(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                userInfoEntity.getImageUrl(),
                userInfoEntity.getNickName()
        );
    }

    public static UserInfoEntity toUserInfoEntity(UserInfoEntity userInfoEntity, OauthSignUpRequestDTO oauthSignUpRequestDTO){
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

    public static UserInfoPass toUserInfoPass(UserInfoEntity userInfoEntity){
        return new UserInfoPass(
                userInfoEntity.getEmail(),
                userInfoEntity.getUsername(),
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
