package com.mog.authserver.company.service;

import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.dto.request.CompanySignUpRequestDTO;
import com.mog.authserver.company.dto.response.CompanyAuthInfoResponseDTO;
import com.mog.authserver.company.mapper.CompanyMapper;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyAuthService {
    private final PasswordEncoder passwordEncoder;
    private final CompanyPersistService companyPersistService;
    private final CompanyValidateService companyValidateService;
    private final JwtService jwtService;

    public void signUP(CompanySignUpRequestDTO companySignUpRequestDTO) {
        if (companyValidateService.doesEmailExist(companySignUpRequestDTO.email())) {
            throw new RuntimeException("이미 존재하는 회사 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(companySignUpRequestDTO.password());
        CompanyEntity companyEntity = CompanyMapper.toCompanyEntity(companySignUpRequestDTO, encodedPassword);
        companyPersistService.save(companyEntity);
    }

    public void signOut(String refreshToken) {
        jwtService.storeRefreshToken(refreshToken);
    }

    public JwtToken refreshAuth(String refreshToken) {
        jwtService.validateRefreshTokenExistence(refreshToken);
        return jwtService.reGenerateTokenSet(refreshToken);
    }

    public CompanyAuthInfoResponseDTO getCompanyAuthInfo(Long id) {
        CompanyEntity companyEntity = companyPersistService.findById(id);
        return CompanyAuthInfoResponseDTO.from(companyEntity);
    }
}
