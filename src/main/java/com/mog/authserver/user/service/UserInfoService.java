package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.exception.UserAlreadyExistException;
import com.mog.authserver.user.exception.UserNotFoundException;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import com.mog.authserver.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserInfoEntity signUp(UserInfoRequestDTO userInfoRequestDTO){
        UserInfoEntity userInfoEntity = UserInfoEntityMapper.toUserInfo(userInfoRequestDTO);
        try {
            findUserInfoByEmailAndLoginSource(userInfoEntity.getEmail(), userInfoEntity.getLoginSource());
            throw new UserAlreadyExistException();
        }
        catch (UserNotFoundException userNotFoundException){
            userInfoEntity.setPassword(passwordEncoder.encode(userInfoEntity.getPassword()));
            return saveUserInfo(userInfoEntity);
        }
    }

    public UserInfoEntity findUserInfoById(Long id) {
        return userInfoRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found With Given ID"));
    }


    public UserInfoEntity findUserInfoByEmailAndLoginSource(String email, LoginSource source)  {
        Optional<UserInfoEntity> userInfoEntityOptional = userInfoRepository.findByEmailAndLoginSource(email, source);
        return userInfoEntityOptional.orElseThrow(() -> new UserNotFoundException("User Not Found With Given Email and Login source"));
    }

    public UserInfoEntity saveUserInfo(UserInfoEntity userInfoEntity){
        return userInfoRepository.save(userInfoEntity);
    }
}
