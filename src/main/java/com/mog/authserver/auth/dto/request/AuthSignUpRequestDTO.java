package com.mog.authserver.auth.dto.request;

import com.mog.authserver.company.dto.request.CompanySignUpRequestDTO;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.request.UserSignUpRequestDTO;

public record AuthSignUpRequestDTO(
        String username,
        String email,
        String rawPwd,
        Role role,
        LoginSource loginSource) {

    public static AuthSignUpRequestDTO from(CompanySignUpRequestDTO companySignUpRequestDTO) {
        return new AuthSignUpRequestDTO(
                companySignUpRequestDTO.companyName(),
                companySignUpRequestDTO.email(),
                companySignUpRequestDTO.password(),
                Role.COMPANY,
                LoginSource.THIS);
    }

    public static AuthSignUpRequestDTO from(UserSignUpRequestDTO userSignUpRequestDTO) {
        return new AuthSignUpRequestDTO(
                userSignUpRequestDTO.username(),
                userSignUpRequestDTO.email(),
                userSignUpRequestDTO.password(),
                Role.USER,
                LoginSource.THIS);
    }

}
