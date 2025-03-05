package com.mog.authserver.company.dto.request;

import com.mog.authserver.company.validator.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public record CompanySignUpRequestDTO(
        @NotBlank
        String companyName,

        @NotBlank
        String email,

        @NotBlank
        String password,

        @ValidPhoneNumber
        @NotBlank
        String phoneNumber,

        @NotBlank
        String address,

        @NotBlank
        String shortDescription,

        @NotBlank
        String description
) {
}
