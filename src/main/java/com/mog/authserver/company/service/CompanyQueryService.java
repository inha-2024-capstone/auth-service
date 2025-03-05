package com.mog.authserver.company.service;

import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.dto.response.CompanyInfoResponseDTO;
import com.mog.authserver.company.dto.response.CompanyPassDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyQueryService {
    private final CompanyPersistService companyPersistService;

    public Page<CompanyInfoResponseDTO> getCompanyInfoPaging(Integer page, Integer size) {
        Page<CompanyEntity> allWithPaging = companyPersistService.findAllWithPaging(page, size);
        return allWithPaging.map(CompanyInfoResponseDTO::from);
    }

    public CompanyInfoResponseDTO getCompanyInfo(Long id) {
        CompanyEntity companyEntity = companyPersistService.findByAuthId(id);
        return CompanyInfoResponseDTO.from(companyEntity);
    }

    public CompanyPassDTO getCompanyPass(Long id) {
        CompanyEntity companyEntity = companyPersistService.findByAuthId(id);
        return CompanyPassDTO.from(companyEntity);
    }
}
