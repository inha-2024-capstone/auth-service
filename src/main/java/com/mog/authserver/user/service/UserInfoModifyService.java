package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoModifyService {
    private final UserInfoPersistService userInfoPersistService;
    private final UserInfoValidationService userInfoValidationService;
    private final PasswordEncoder passwordEncoder;

    public void modifyPassword(Long id, String password) {
        if (userInfoValidationService.validateSamePassword(id, password)) {
            throw new RuntimeException("기존의 비밀번호와 일치합니다.");
        }

        UserInfoEntity userInfoById = userInfoPersistService.findById(id);
        String encodedPassword = passwordEncoder.encode(password);
        UserInfoEntity updatedUserInfoEntity = UserInfoEntityMapper.updatePassword(userInfoById, encodedPassword);
        userInfoPersistService.save(updatedUserInfoEntity);
    }

    public void modifyNickname(Long id, String nickname) {
        if (userInfoValidationService.doesNickNameExist(nickname)){
            throw new RuntimeException("닉네임이 이미 존재합니다.");
        }
        UserInfoEntity userInfoEntityById = userInfoPersistService.findById(id);
        UserInfoEntity nicknameUpdated = UserInfoEntityMapper.updateNickname(userInfoEntityById, nickname);
        userInfoPersistService.save(nicknameUpdated);
    }

    public void modifyProfileImage(MultipartFile multipartFile) {
    }
}
