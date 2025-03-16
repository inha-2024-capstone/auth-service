package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.dto.response.UserInfoResponseDTO;
import com.mog.authserver.user.dto.response.UserPassDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager", readOnly = true)
public class UserInfoQueryService {
    private final UserInfoPersistService userInfoPersistService;

    public UserInfoResponseDTO findUserInfoById(Long id) {
        return UserInfoResponseDTO.from(userInfoPersistService.findByAuthId(id));
    }

    public UserInfoResponseDTO findOauth2UserInfoById(Long id) {
        UserInfoEntity userInfoEntity = userInfoPersistService.findByAuthId(id);
        if (userInfoEntity.getAuthEntity().getLoginSource() == LoginSource.THIS) {
            throw new RuntimeException("해당 사용자는 OAuth2.0 사용자가 아닙니다.");
        }
        return UserInfoResponseDTO.from(userInfoEntity);
    }

    public UserPassDTO getUserPass(Long id) {
        UserInfoEntity byAuthId = userInfoPersistService.findByAuthId(id);
        return UserPassDTO.from(byAuthId);
    }
}
