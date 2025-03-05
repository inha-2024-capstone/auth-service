package com.mog.authserver.company.dto.response;

import com.mog.authserver.company.domain.CompanyEntity;

public record CompanyAuthInfoResponseDTO(
        String companyName,
        String email,
        String phoneNumber,
        String address,
        String description,
        String shortDescription,
        String imageUrl
) {

    public static CompanyAuthInfoResponseDTO from(CompanyEntity companyEntity) {
        return new CompanyAuthInfoResponseDTO(
                companyEntity.getAuthEntity().getUsername(),
                companyEntity.getAuthEntity().getEmail(),
                companyEntity.getPhoneNumber(),
                companyEntity.getAddress(),
                companyEntity.getDescription(),
                companyEntity.getShortDescription(),
                companyEntity.getImageUrl()
        );
    }
}
