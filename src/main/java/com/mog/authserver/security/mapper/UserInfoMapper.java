package com.mog.authserver.security.mapper;

import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class UserInfoMapper {

    public static AuthenticatedUserInfo toAuthenticatedUserInfo(UserInfoEntity userInfoEntity){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userInfoEntity.getRole().name()));

        return new AuthenticatedUserInfo(
                userInfoEntity.getId(),
                userInfoEntity.getNickName(),
                authorities
        );
    }
}
