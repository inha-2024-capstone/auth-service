package com.mog.authserver.company.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyValidateService {
    private final CompanyPersistService companyPersistService;
    private final PasswordEncoder passwordEncoder;
}
