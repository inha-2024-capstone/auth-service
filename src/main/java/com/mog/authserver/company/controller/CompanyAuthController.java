package com.mog.authserver.company.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.company.dto.request.CompanySignUpRequestDTO;
import com.mog.authserver.company.dto.response.CompanyAuthInfoResponseDTO;
import com.mog.authserver.company.service.CompanyAuthService;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyAuthController {
    private final CompanyAuthService companyAuthService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<Void>> signUp(
            @Valid @RequestBody CompanySignUpRequestDTO companySignUpRequestDTO) {
        companyAuthService.signUP(companySignUpRequestDTO);
        return SuccessStatus.CREATED.getResponseBody();
    }

    @GetMapping("/auth-info")
    public ResponseEntity<BaseResponseBody<CompanyAuthInfoResponseDTO>> getCompanyAuthInfo(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo) {
        CompanyAuthInfoResponseDTO companyInfo = companyAuthService.getCompanyAuthInfo(authenticatedUserInfo.id());
        return SuccessStatus.OK.getResponseBody(companyInfo);
    }
}
