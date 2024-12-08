package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.exception.UserNotFoundException;
import com.mog.authserver.user.repository.UserInfoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoPersistService {
    private final UserInfoRepository userInfoRepository;

    @Cacheable(cacheNames = "userInfoCache", key = "#id", condition = "#id != null")
    public UserInfoEntity findById(Long id) {
        return userInfoRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found With Given ID"));
    }

    @CachePut(cacheNames = "userInfoCache", key = "#result.id", unless = "#result == null")
    public UserInfoEntity save(UserInfoEntity userInfoEntity) {
        return userInfoRepository.save(userInfoEntity);
    }

    public UserInfoEntity findByEmailAndLoginSource(String email, LoginSource source) {
        Optional<UserInfoEntity> userInfoEntityOptional = userInfoRepository.findByEmailAndLoginSource(email, source);
        return userInfoEntityOptional.orElseThrow(
                () -> new UserNotFoundException("User Not Found With Given Email and Login source"));
    }

    public boolean existsByEmailAndLoginSource(String email, LoginSource loginSource) {
        return userInfoRepository.existsByEmailAndLoginSource(email, loginSource);
    }

    public boolean existsByNickname(String nickname) {
        return userInfoRepository.existsByNickName(nickname);
    }
}
