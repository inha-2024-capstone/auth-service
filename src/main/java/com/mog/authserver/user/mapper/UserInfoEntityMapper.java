package com.mog.authserver.user.mapper;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.dto.UserInfoResponseDTO;
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
    public static UserInfoEntity toUserInfo(UserInfoRequestDTO userInfoRequestDTO){
        return new UserInfoEntity(
                userInfoRequestDTO.email(),
                userInfoRequestDTO.username(),
                userInfoRequestDTO.password(),
                userInfoRequestDTO.role(),
                userInfoRequestDTO.gender(),
                userInfoRequestDTO.phoneNumber(),
                userInfoRequestDTO.address(),
                userInfoRequestDTO.nickName(),
                null,
                userInfoRequestDTO.loginSource()
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

    public static UserInfoRequestDTO toUserInfoRequestDTO(UserInfoEntity userInfoEntity){
        return new UserInfoRequestDTO(
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
