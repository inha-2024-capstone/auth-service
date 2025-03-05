package com.mog.authserver.company.service;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.dto.request.AuthSignUpRequestDTO;
import com.mog.authserver.auth.service.AuthRegisterService;
import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.dto.request.CompanySignUpRequestDTO;
import com.mog.authserver.company.dto.response.CompanyAuthInfoResponseDTO;
import com.mog.authserver.company.mapper.CompanyMapper;
import com.mog.authserver.gcs.constant.GcsImages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyAuthService {
    private final CompanyPersistService companyPersistService;
    private final AuthRegisterService authRegisterService;
    private final GcsImages gcsImages;

    @Transactional
    public void signUP(CompanySignUpRequestDTO companySignUpRequestDTO) {
        AuthSignUpRequestDTO authSignUpRequestDTO = AuthSignUpRequestDTO.from(companySignUpRequestDTO);
        AuthEntity authEntity = authRegisterService.signUp(authSignUpRequestDTO);
        CompanyEntity companyEntity = CompanyMapper.createCompanyEntity(companySignUpRequestDTO, authEntity,
                gcsImages.DEFAULT_COMPANY_IMAGE);
        companyPersistService.save(companyEntity);
    }

    public CompanyAuthInfoResponseDTO getCompanyAuthInfo(Long id) {
        CompanyEntity companyEntity = companyPersistService.findByAuthId(id);
        return CompanyAuthInfoResponseDTO.from(companyEntity);
    }
}
