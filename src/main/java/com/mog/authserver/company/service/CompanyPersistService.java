package com.mog.authserver.company.service;

import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyPersistService {
    private final CompanyRepository companyRepository;

    @Cacheable(cacheNames = "companyCache", key = "#id")
    public CompanyEntity findByAuthId(Long id) {
        return companyRepository.findByAuthEntity_Id(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 CompanyEntity가 존재하지 않습니다."));
    }

    @CachePut(cacheNames = "companyCache", key = "#result.id", unless = "#result == null")
    public CompanyEntity save(CompanyEntity companyEntity) {
        return companyRepository.save(companyEntity);
    }

    public Page<CompanyEntity> findAllWithPaging(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.findAll(pageable);
    }
}
