package com.mog.authserver.user.service;

import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.gcs.service.GcsImageService;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoModifyService {
    private final UserInfoPersistService userInfoPersistService;
    private final UserInfoValidationService userInfoValidationService;
    private final GcsImageService gcsImageService;

    public void modifyNickname(Long id, String nickname) {
        if (userInfoValidationService.doesNickNameExist(nickname)) {
            throw new RuntimeException("닉네임이 이미 존재합니다.");
        }
        UserInfoEntity userInfoEntityById = userInfoPersistService.findByAuthId(id);
        UserInfoEntity nicknameUpdated = UserInfoEntityMapper.updateNickname(userInfoEntityById, nickname);
        userInfoPersistService.save(nicknameUpdated);
    }

    public void updateProfileImage(Long id, MultipartFile multipartFile) {
        UserInfoEntity byAuthId = userInfoPersistService.findByAuthId(id);
        deleteImage(id);
        String imageUrl = gcsImageService.uploadFile(multipartFile);
        UserInfoEntity imageUpdated = UserInfoEntityMapper.updateImage(byAuthId, imageUrl);
        userInfoPersistService.save(imageUpdated);
    }

    public void deleteAndUpdateDefaultImage(Long id) {
        UserInfoEntity byAuthId = userInfoPersistService.findByAuthId(id);
        deleteImage(id);
        UserInfoEntity userInfoEntity = UserInfoEntityMapper.updateImage(byAuthId, Constant.DEFAULT_COMPANY_IMAGE);
        userInfoPersistService.save(userInfoEntity);
    }

    private void deleteImage(Long id) {
        UserInfoEntity byAuthId = userInfoPersistService.findByAuthId(id);
        if (byAuthId.getImageUrl() != null && !byAuthId.getImageUrl()
                .equals(Constant.DEFAULT_COMPANY_IMAGE)) {
            gcsImageService.deleteFile(byAuthId.getImageUrl());
        }
    }
}
