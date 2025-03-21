package com.mog.authserver.user.service;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.dto.request.AuthSignUpRequestDTO;
import com.mog.authserver.auth.event.UserUpsertEvent;
import com.mog.authserver.auth.producer.UserUpsertProducer;
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
public class UserInfoAuthService {
    private final UserInfoPersistService userInfoPersistService;
    private final AuthRegisterService authRegisterService;
    private final UserUpsertProducer userUpsertProducer;
    private final UserInfoValidationService userInfoValidationService;
    private final GcsImages gcsImages;

    @Transactional(transactionManager = "transactionManager")
    public void signUp(UserSignUpRequestDTO userSignUpRequestDTO) {
        if(userInfoValidationService.doesNickNameExist(userSignUpRequestDTO.nickName())){
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        AuthSignUpRequestDTO authSignUpRequestDTO = AuthSignUpRequestDTO.from(userSignUpRequestDTO);
        AuthEntity authEntity = authRegisterService.signUp(authSignUpRequestDTO);
        UserInfoEntity userInfoEntity = UserInfoEntityMapper.createUserInfoEntity(userSignUpRequestDTO, authEntity,
                gcsImages.DEFAULT_USER_IMAGE);

        UserInfoEntity saved = userInfoPersistService.save(userInfoEntity);
        userUpsertProducer.publishUserUpsert(UserUpsertEvent.from(saved));
    }

    @Transactional(transactionManager = "transactionManager")
    public void oAuthSignUp(UserOauthSignUpRequestDTO userOauthSignUpRequestDTO, Long id) {
        UserInfoEntity byAuthId = userInfoPersistService.findByAuthId(id);
        UserInfoEntity updatedUserInfoEntity = UserInfoEntityMapper.createUserInfoEntity(userOauthSignUpRequestDTO,
                byAuthId);

        UserInfoEntity saved = userInfoPersistService.save(updatedUserInfoEntity);
        userUpsertProducer.publishUserUpsert(UserUpsertEvent.from(saved));
    }
}
