package com.mog.authserver.company.service;

import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyPersistService {
    private final CompanyRepository companyRepository;

    public CompanyEntity findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 CompanyEntity가 존재하지 않습니다."));
    }

    public CompanyEntity findByEmail(String email) {
        return companyRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 email의 CompanyEntity가 존재하지 않습니다."));
    }

    public CompanyEntity save(CompanyEntity companyEntity) {
        return companyRepository.save(companyEntity);
    }

    public Page<CompanyEntity> findAllWithPaging(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.findAll(pageable);
    }

    public boolean existsByEmail(String email) {
        return companyRepository.existsByEmail(email);
    }
}
