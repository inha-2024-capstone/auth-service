package com.mog.authserver.user.service;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.dto.request.AuthSignUpRequestDTO;
import com.mog.authserver.auth.service.AuthRegisterService;
import com.mog.authserver.gcs.constant.GcsImages;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.dto.request.UserOauthSignUpRequestDTO;
import com.mog.authserver.user.dto.request.UserSignUpRequestDTO;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInfoAuthService {
    private final UserInfoPersistService userInfoPersistService;
    private final AuthRegisterService authRegisterService;
    private final GcsImages gcsImages;

    @Transactional(readOnly = false)
    public void signUp(UserSignUpRequestDTO userSignUpRequestDTO) {

        AuthSignUpRequestDTO authSignUpRequestDTO = AuthSignUpRequestDTO.from(userSignUpRequestDTO);
        AuthEntity authEntity = authRegisterService.signUp(authSignUpRequestDTO);
        UserInfoEntity userInfoEntity = UserInfoEntityMapper.createUserInfoEntity(userSignUpRequestDTO, authEntity,
                gcsImages.DEFAULT_USER_IMAGE);

        userInfoPersistService.save(userInfoEntity);
    }

    @Transactional(readOnly = false)
    public void oAuthSignUp(UserOauthSignUpRequestDTO userOauthSignUpRequestDTO, Long id) {
        UserInfoEntity byAuthId = userInfoPersistService.findByAuthId(id);
        UserInfoEntity updatedUserInfoEntity = UserInfoEntityMapper.createUserInfoEntity(userOauthSignUpRequestDTO,
                byAuthId);

        userInfoPersistService.save(updatedUserInfoEntity);
    }
}
