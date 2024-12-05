package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.dto.request.OauthSignUpRequestDTO;
import com.mog.authserver.user.dto.request.SignUpRequestDTO;
import com.mog.authserver.user.dto.response.UserInfoResponseDTO;
import com.mog.authserver.user.exception.UserAlreadyExistException;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import com.mog.authserver.user.pass.UserInfoPass;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInfoService {
    private final PasswordEncoder passwordEncoder;
    private final UserInfoPersistService userInfoPersistService;

    @Transactional(readOnly = false)
    public void signUp(SignUpRequestDTO signUpRequestDTO) {
        UserInfoEntity userInfoEntity = UserInfoEntityMapper.toUserInfoEntity(signUpRequestDTO);
        boolean doesUserExist = userInfoPersistService.existsByEmailAndLoginSource(userInfoEntity);
        if (doesUserExist) {
            throw new UserAlreadyExistException(userInfoEntity.getEmail());
        }
        UserInfoEntity passwordEncodedEntity = UserInfoEntityMapper.updatePassword(userInfoEntity,
                passwordEncoder.encode(userInfoEntity.getPassword()));
        userInfoPersistService.save(passwordEncodedEntity);
    }

    @Transactional(readOnly = false)
    public void oAuthSignUp(OauthSignUpRequestDTO oauthSignUpRequestDTO, Long id) {
        UserInfoEntity userInfoById = userInfoPersistService.findById(id);
        UserInfoEntity updatedUserInfoEntity = UserInfoEntityMapper.updateUserInfoEntity(userInfoById, oauthSignUpRequestDTO);
        userInfoPersistService.save(updatedUserInfoEntity);
    }

    public UserInfoResponseDTO findUserInfoById(Long id) {
        UserInfoEntity userInfoEntity = userInfoPersistService.findById(id);
        return UserInfoResponseDTO.from(userInfoEntity);
    }

    public UserInfoPass findUserInfoPass(Long id) {
        UserInfoEntity userInfoEntity = userInfoPersistService.findById(id);
        return UserInfoPass.from(userInfoEntity);
    }

    public UserInfoResponseDTO findOauth2UserInfoById(Long id) {
        UserInfoEntity userInfoEntity = userInfoPersistService.findById(id);
        if (userInfoEntity.getLoginSource() == LoginSource.THIS) {
            throw new RuntimeException("해당 사용자는 OAuth2.0 사용자가 아닙니다.");
        }
        return UserInfoResponseDTO.from(userInfoEntity);
    }
}
