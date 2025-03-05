package com.mog.authserver.company.service;

import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.dto.request.ImageModifyRequestDTO;
import com.mog.authserver.company.mapper.CompanyMapper;
import com.mog.authserver.gcs.constant.GcsImages;
import com.mog.authserver.gcs.service.GcsImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class CompanyModifyService {
    private final CompanyPersistService companyPersistService;
    private final GcsImageService gcsImageService;
    private final GcsImages gcsImages;

    public void updateDescription(Long id, String description) {
        CompanyEntity companyEntity = companyPersistService.findByAuthId(id);
        CompanyEntity descriptionUpdated = CompanyMapper.updateDescription(companyEntity, description);
        companyPersistService.save(descriptionUpdated);
    }

    public void updateShortDescription(Long id, String shortDescription) {
        CompanyEntity companyEntity = companyPersistService.findByAuthId(id);
        CompanyEntity descriptionUpdated = CompanyMapper.updateShortDescription(companyEntity, shortDescription);
        companyPersistService.save(descriptionUpdated);
    }

    public void updateProfileImage(Long id, ImageModifyRequestDTO imageModifyRequestDTO) {
        CompanyEntity companyEntity = companyPersistService.findByAuthId(id);
        deleteImage(id);
        String imageUrl = gcsImageService.uploadFile(imageModifyRequestDTO.image());
        CompanyEntity descriptionUpdated = CompanyMapper.updateImageUrl(companyEntity, imageUrl);
        companyPersistService.save(descriptionUpdated);
    }

    public void deleteAndUpdateDefaultImage(Long id) {
        CompanyEntity companyEntity = companyPersistService.findByAuthId(id);
        deleteImage(id);
        CompanyEntity descriptionUpdated = CompanyMapper.updateImageUrl(companyEntity, gcsImages.DEFAULT_COMPANY_IMAGE);
        companyPersistService.save(descriptionUpdated);
    }

    private void deleteImage(Long id) {
        CompanyEntity companyEntity = companyPersistService.findByAuthId(id);
        if (companyEntity.getImageUrl() != null && !companyEntity.getImageUrl()
                .equals(gcsImages.DEFAULT_COMPANY_IMAGE)) {
            gcsImageService.deleteFile(companyEntity.getImageUrl());
        }
    }
}
