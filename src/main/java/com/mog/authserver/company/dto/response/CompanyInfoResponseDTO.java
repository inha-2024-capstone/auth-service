package com.mog.authserver.company.dto.response;

import com.mog.authserver.company.domain.CompanyEntity;

public record CompanyInfoResponseDTO(
        Long id,
        String companyName,
        String email,
        String phoneNumber,
        String address,
        String description,
        String shortDescription,
        String imageUrl) {

    public static CompanyInfoResponseDTO from(CompanyEntity companyEntity) {
        return new CompanyInfoResponseDTO(
                companyEntity.getAuthEntity().getId(),
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
