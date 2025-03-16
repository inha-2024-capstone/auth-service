package com.mog.authserver.user.service;

import com.mog.authserver.auth.event.UserUpsertEvent;
import com.mog.authserver.auth.producer.UserUpsertProducer;
import com.mog.authserver.gcs.constant.GcsImages;
import com.mog.authserver.gcs.service.GcsImageService;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserInfoModifyService {
    private final UserInfoPersistService userInfoPersistService;
    private final UserInfoValidationService userInfoValidationService;
    private final GcsImageService gcsImageService;
    private final UserUpsertProducer userUpsertProducer;
    private final GcsImages gcsImages;

    @Transactional(transactionManager = "transactionManager")
    public void modifyNickname(Long id, String nickname) {
        if (userInfoValidationService.doesNickNameExist(nickname)) {
            throw new RuntimeException("닉네임이 이미 존재합니다.");
        }
        UserInfoEntity userInfoEntityById = userInfoPersistService.findByAuthId(id);
        UserInfoEntity nicknameUpdated = UserInfoEntityMapper.updateNickname(userInfoEntityById, nickname);
        UserInfoEntity saved = userInfoPersistService.save(nicknameUpdated);
        userUpsertProducer.publishUserUpsert(UserUpsertEvent.from(saved));
    }

    @Transactional(transactionManager = "transactionManager")
    public void updateProfileImage(Long id, MultipartFile multipartFile) {
        UserInfoEntity byAuthId = userInfoPersistService.findByAuthId(id);
        deleteImage(id);
        String imageUrl = gcsImageService.uploadFile(multipartFile);
        UserInfoEntity imageUpdated = UserInfoEntityMapper.updateImage(byAuthId, imageUrl);
        UserInfoEntity saved = userInfoPersistService.save(imageUpdated);
        userUpsertProducer.publishUserUpsert(UserUpsertEvent.from(saved));
    }

    @Transactional(transactionManager = "transactionManager")
    public void deleteAndUpdateDefaultImage(Long id) {
        UserInfoEntity byAuthId = userInfoPersistService.findByAuthId(id);
        deleteImage(id);
        UserInfoEntity userInfoEntity = UserInfoEntityMapper.updateImage(byAuthId, gcsImages.DEFAULT_USER_IMAGE);
        UserInfoEntity saved = userInfoPersistService.save(userInfoEntity);
        userUpsertProducer.publishUserUpsert(UserUpsertEvent.from(saved));
    }

    private void deleteImage(Long id) {
        UserInfoEntity byAuthId = userInfoPersistService.findByAuthId(id);
        if (byAuthId.getImageUrl() != null && !byAuthId.getImageUrl()
                .equals(gcsImages.DEFAULT_USER_IMAGE)) {
            gcsImageService.deleteFile(byAuthId.getImageUrl());
        }
    }
}
