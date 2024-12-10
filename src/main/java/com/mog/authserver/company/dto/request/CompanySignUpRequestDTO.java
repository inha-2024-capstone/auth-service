package com.mog.authserver.company.dto.request;

import com.mog.authserver.company.validator.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanySignUpRequestDTO(
        @NotNull @NotBlank
        String companyName,

        @NotNull @NotBlank
        String email,

        @NotNull @NotBlank
        String password,

        @ValidPhoneNumber
        @NotNull @NotBlank
        String phoneNumber,

        @NotNull @NotBlank
        String address,

        @NotNull @NotBlank
        String shortDescription,

        @NotNull @NotBlank
        String description
) {
}
