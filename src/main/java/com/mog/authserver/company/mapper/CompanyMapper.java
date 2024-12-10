package com.mog.authserver.company.mapper;

import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.dto.request.CompanySignUpRequestDTO;
import com.mog.authserver.user.domain.enums.Role;

public class CompanyMapper {

    public static CompanyEntity toCompanyEntity(CompanySignUpRequestDTO signUpRequestDTO, String encodedPassword) {
        return new CompanyEntity(
                signUpRequestDTO.companyName(),
                signUpRequestDTO.email(),
                encodedPassword,
                signUpRequestDTO.phoneNumber(),
                signUpRequestDTO.address(),
                signUpRequestDTO.description(),
                signUpRequestDTO.shortDescription(),
                Constant.DEFAULT_COMPANY_IMAGE,
                Role.COMPANY
        );
    }

    public static CompanyEntity updatePassword(CompanyEntity companyEntity, String encodedPassword) {
        return new CompanyEntity(
                companyEntity.getId(),
                companyEntity.getCreateTime(),
                companyEntity.getModifiedTime(),
                companyEntity.getDeletedTime(),
                companyEntity.getState(),
                companyEntity.getCompanyName(),
                companyEntity.getEmail(),
                encodedPassword,
                companyEntity.getPhoneNumber(),
                companyEntity.getAddress(),
                companyEntity.getDescription(),
                companyEntity.getShortDescription(),
                companyEntity.getImageUrl(),
                companyEntity.getRole()
        );
    }

    public static CompanyEntity updateDescription(CompanyEntity companyEntity, String description) {
        return new CompanyEntity(
                companyEntity.getId(),
                companyEntity.getCreateTime(),
                companyEntity.getModifiedTime(),
                companyEntity.getDeletedTime(),
                companyEntity.getState(),
                companyEntity.getCompanyName(),
                companyEntity.getEmail(),
                companyEntity.getPassword(),
                companyEntity.getPhoneNumber(),
                companyEntity.getAddress(),
                description,
                companyEntity.getShortDescription(),
                companyEntity.getImageUrl(),
                companyEntity.getRole()
        );
    }

    public static CompanyEntity updateShortDescription(CompanyEntity companyEntity, String shortDescription) {
        return new CompanyEntity(
                companyEntity.getId(),
                companyEntity.getCreateTime(),
                companyEntity.getModifiedTime(),
                companyEntity.getDeletedTime(),
                companyEntity.getState(),
                companyEntity.getCompanyName(),
                companyEntity.getEmail(),
                companyEntity.getPassword(),
                companyEntity.getPhoneNumber(),
                companyEntity.getAddress(),
                companyEntity.getDescription(),
                shortDescription,
                companyEntity.getImageUrl(),
                companyEntity.getRole()
        );
    }

    public static CompanyEntity updateImageUrl(CompanyEntity companyEntity, String imageUrl) {
        return new CompanyEntity(
                companyEntity.getId(),
                companyEntity.getCreateTime(),
                companyEntity.getModifiedTime(),
                companyEntity.getDeletedTime(),
                companyEntity.getState(),
                companyEntity.getCompanyName(),
                companyEntity.getEmail(),
                companyEntity.getPassword(),
                companyEntity.getPhoneNumber(),
                companyEntity.getAddress(),
                companyEntity.getDescription(),
                companyEntity.getShortDescription(),
                imageUrl,
                companyEntity.getRole()
        );
    }
}
