package com.mog.authserver.company.service;

import com.mog.authserver.company.domain.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyValidateService {
    private final CompanyPersistService companyPersistService;
    private final PasswordEncoder passwordEncoder;

    public Boolean doesEmailExist(String email) {
        return companyPersistService.existsByEmail(email);
    }

    public Boolean isSamePassword(Long id, String password) {
        CompanyEntity companyEntity = companyPersistService.findById(id);
        return passwordEncoder.matches(password, companyEntity.getPassword());
    }
}
