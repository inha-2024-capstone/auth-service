package com.mog.authserver.user.mapper;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.dto.UserInfoModifyDTO;
import com.mog.authserver.user.dto.UserInfoResponseDTO;
import com.mog.authserver.user.dto.UserInfoSignUpDTO;
import com.mog.authserver.user.pass.UserInfoPass;

public class UserInfoEntityMapper {
    public static UserInfoEntity toUserInfo(UserInfoResponseDTO userInfoResponseDTO){
        return new UserInfoEntity(
                userInfoResponseDTO.email(),
                userInfoResponseDTO.username(),
                null,
                userInfoResponseDTO.role(),
                userInfoResponseDTO.gender(),
                userInfoResponseDTO.phoneNumber(),
                userInfoResponseDTO.address(),
                userInfoResponseDTO.nickName(),
                null,
                userInfoResponseDTO.loginSource()
        );
    }
    public static UserInfoEntity toUserInfo(UserInfoSignUpDTO userInfoSignUpRequestDTO){
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
                userInfoEntity.getRole(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                userInfoEntity.getNickName(),
                userInfoEntity.getLoginSource()
        );
    }

    public static UserInfoSignUpDTO toUserInfoRequestDTO(UserInfoEntity userInfoEntity){
        return new UserInfoSignUpDTO(
                userInfoEntity.getEmail(),
                userInfoEntity.getUsername(),
                userInfoEntity.getPassword(),
                userInfoEntity.getRole(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                userInfoEntity.getNickName(),
                userInfoEntity.getLoginSource()
        );
    }

    public static UserInfoEntity toUserInfoEntity(UserInfoEntity userInfoEntity, UserInfoModifyDTO userInfoModifyDTO){
        return new UserInfoEntity(
                userInfoEntity.getId(),
                userInfoEntity.getCreateTime(),
                userInfoEntity.getModifiedTime(),
                userInfoEntity.getDeletedTime(),
                userInfoEntity.getState(),
                userInfoEntity.getEmail(),
                userInfoModifyDTO.username(),
                userInfoEntity.getPassword(),
                userInfoModifyDTO.role(),
                userInfoModifyDTO.gender(),
                userInfoModifyDTO.phoneNumber(),
                userInfoModifyDTO.address(),
                userInfoModifyDTO.nickName(),
                userInfoModifyDTO.address(),
                userInfoModifyDTO.loginSource()
        );
    }

    public static UserInfoModifyDTO toUserInfoModifyDTO(UserInfoEntity userInfoEntity){
        return new UserInfoModifyDTO(
                userInfoEntity.getEmail(),
                userInfoEntity.getUsername(),
                userInfoEntity.getRole(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                userInfoEntity.getNickName(),
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
