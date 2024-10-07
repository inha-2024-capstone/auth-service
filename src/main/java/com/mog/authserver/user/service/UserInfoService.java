package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.exception.UserAlreadyExistException;
import com.mog.authserver.user.exception.UserNotFoundException;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import com.mog.authserver.user.pass.UserInfoPass;
import com.mog.authserver.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoEntity oAuthSignUp(UserInfoRequestDTO userInfoRequestDTO, Long id){
        UserInfoEntity userInfoById = this.findUserInfoById(id);
        userInfoById.setUserInfoEntityByUserInfoRequest(userInfoRequestDTO);
        return userInfoRepository.save(userInfoById);
    }

    public UserInfoEntity signUp(UserInfoRequestDTO userInfoRequestDTO){
        UserInfoEntity userInfoEntity = UserInfoEntityMapper.toUserInfo(userInfoRequestDTO);
        Boolean doesUserExist = userInfoRepository.existsByEmailAndLoginSource(userInfoEntity.getEmail(), userInfoEntity.getLoginSource());
        if(doesUserExist){
            throw new UserAlreadyExistException(userInfoEntity.getEmail());
        }
        else {
            userInfoEntity.setPassword(passwordEncoder.encode(userInfoEntity.getPassword()));
            userInfoRepository.save(userInfoEntity);
            return userInfoEntity;
        }
    }

    public UserInfoEntity findUserInfoById(Long id) {
        return userInfoRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found With Given ID"));
    }

    public UserInfoEntity findUserInfoByEmailAndLoginSource(String email, LoginSource source) throws UserNotFoundException {
        Optional<UserInfoEntity> userInfoEntityOptional = userInfoRepository.findByEmailAndLoginSource(email, source);
        return userInfoEntityOptional.orElseThrow(() -> new UserNotFoundException("User Not Found With Given Email and Login source"));
    }

    public Boolean existUserInfoByEmailAndLoginSource(String email, LoginSource source) throws UserNotFoundException {
        return userInfoRepository.existsByEmailAndLoginSource(email, source);
    }

    public UserInfoEntity saveUserInfo(UserInfoEntity userInfoEntity){
        return userInfoRepository.save(userInfoEntity);
    }

    public void deleteUserInfo(UserInfoEntity userInfoEntity){
        userInfoRepository.delete(userInfoEntity);
    }

    public UserInfoPass findUserInfoPass(Long id) {
        UserInfoEntity userInfoById = this.findUserInfoById(id);
        return  UserInfoEntityMapper.toUserInfoPass(userInfoById);
    }
}
