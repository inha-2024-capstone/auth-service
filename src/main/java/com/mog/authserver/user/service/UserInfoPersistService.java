package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.exception.UserNotFoundException;
import com.mog.authserver.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoPersistService {
    private final UserInfoRepository userInfoRepository;

    @CachePut(cacheNames = "userInfoCache", key = "#result.id", unless = "#result == null")
    public UserInfoEntity save(UserInfoEntity userInfoEntity) {
        return userInfoRepository.save(userInfoEntity);
    }

    @Cacheable(cacheNames = "userInfoCache", key = "#id")
    public UserInfoEntity findByAuthId(Long id) {
        return userInfoRepository.findByAuthEntity_Id(id)
                .orElseThrow(() -> new UserNotFoundException("해당 Auth ID에 해당하는 유저가 없습니다."));
    }

    public UserInfoEntity findByEmailAndLoginSource(String email, LoginSource loginSource) {
        return userInfoRepository.findByAuthEntityEmailAndAuthEntityLoginSource(email, loginSource)
                .orElseThrow(() -> new UserNotFoundException("해당 email과 loginSource에 해당하는 유저가 없습니다."));
    }

    public boolean existsByNickname(String nickname) {
        return userInfoRepository.existsByNickName(nickname);
    }
}
