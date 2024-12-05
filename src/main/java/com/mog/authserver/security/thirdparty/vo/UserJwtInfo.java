package com.mog.authserver.security.thirdparty.vo;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Role;

public record UserJwtInfo(Long id, Role role, String email) {

    public static UserJwtInfo from(UserInfoEntity userInfoEntity) {
        return new UserJwtInfo(userInfoEntity.getId(), userInfoEntity.getRole(), userInfoEntity.getEmail());
    }
}
