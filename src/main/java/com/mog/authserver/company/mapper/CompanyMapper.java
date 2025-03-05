package com.mog.authserver.company.mapper;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.dto.request.CompanySignUpRequestDTO;

public class CompanyMapper {

    public static CompanyEntity createCompanyEntity(CompanySignUpRequestDTO signUpRequestDTO, AuthEntity authEntity,
                                                    String baseComImage) {
        return new CompanyEntity(
                null,
                signUpRequestDTO.phoneNumber(),
                signUpRequestDTO.address(),
                signUpRequestDTO.description(),
                signUpRequestDTO.shortDescription(),
                baseComImage,
                authEntity
        );
    }

    public static CompanyEntity updateDescription(CompanyEntity companyEntity, String description) {
        return new CompanyEntity(
                companyEntity.getCreateTime(),
                companyEntity.getModifiedTime(),
                companyEntity.getDeletedTime(),
                companyEntity.getState(),
                companyEntity.getId(),
                companyEntity.getPhoneNumber(),
                companyEntity.getAddress(),
                description,
                companyEntity.getShortDescription(),
                companyEntity.getImageUrl(),
                companyEntity.getAuthEntity()
        );
    }

    public static CompanyEntity updateShortDescription(CompanyEntity companyEntity, String shortDescription) {
        return new CompanyEntity(
                companyEntity.getCreateTime(),
                companyEntity.getModifiedTime(),
                companyEntity.getDeletedTime(),
                companyEntity.getState(),
                companyEntity.getId(),
                companyEntity.getPhoneNumber(),
                companyEntity.getAddress(),
                companyEntity.getDescription(),
                shortDescription,
                companyEntity.getImageUrl(),
                companyEntity.getAuthEntity()
        );
    }

    public static CompanyEntity updateImageUrl(CompanyEntity companyEntity, String imageUrl) {
        return new CompanyEntity(
                companyEntity.getCreateTime(),
                companyEntity.getModifiedTime(),
                companyEntity.getDeletedTime(),
                companyEntity.getState(),
                companyEntity.getId(),
                companyEntity.getPhoneNumber(),
                companyEntity.getAddress(),
                companyEntity.getDescription(),
                companyEntity.getShortDescription(),
                imageUrl,
                companyEntity.getAuthEntity()
        );
    }
}
