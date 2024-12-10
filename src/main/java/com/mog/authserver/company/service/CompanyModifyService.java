package com.mog.authserver.company.service;

import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.dto.request.ImageModifyRequestDTO;
import com.mog.authserver.company.mapper.CompanyMapper;
import com.mog.authserver.gcs.service.GcsImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyModifyService {
    private final CompanyValidateService companyValidateService;
    private final CompanyPersistService companyPersistService;
    private final GcsImageService gcsImageService;
    private final PasswordEncoder passwordEncoder;

    public void updatePassword(Long id, String password) {
        if (companyValidateService.isSamePassword(id, password)) {
            throw new RuntimeException("이전과 동일한 비밀번호입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        CompanyEntity companyEntity = companyPersistService.findById(id);
        CompanyEntity passwordUpdated = CompanyMapper.updatePassword(companyEntity, encodedPassword);
        companyPersistService.save(passwordUpdated);
    }

    public void updateDescription(Long id, String description) {
        CompanyEntity companyEntity = companyPersistService.findById(id);
        CompanyEntity descriptionUpdated = CompanyMapper.updateDescription(companyEntity, description);
        companyPersistService.save(descriptionUpdated);
    }

    public void updateShortDescription(Long id, String shortDescription) {
        CompanyEntity companyEntity = companyPersistService.findById(id);
        CompanyEntity descriptionUpdated = CompanyMapper.updateShortDescription(companyEntity, shortDescription);
        companyPersistService.save(descriptionUpdated);
    }

    public void updateProfileImage(Long id, ImageModifyRequestDTO imageModifyRequestDTO) {
        CompanyEntity companyEntity = companyPersistService.findById(id);
        if (companyEntity.getImageUrl() != null) {
            gcsImageService.deleteFile(companyEntity.getImageUrl());
        }
        String imageUrl = gcsImageService.uploadFile(imageModifyRequestDTO.companyImage());
        CompanyEntity descriptionUpdated = CompanyMapper.updateImageUrl(companyEntity, imageUrl);
        companyPersistService.save(descriptionUpdated);
    }
}
