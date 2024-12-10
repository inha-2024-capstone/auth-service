package com.mog.authserver.company.controller;

import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.company.dto.request.CompanySignUpRequestDTO;
import com.mog.authserver.company.dto.response.CompanyAuthInfoResponseDTO;
import com.mog.authserver.company.service.CompanyAuthService;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
        return ResponseEntity.status(SuccessStatus.CREATED.getHttpStatus())
                .body(SuccessStatus.CREATED.getBaseResponseBody());
    }

    @GetMapping("/sign-in")
    public ResponseEntity<BaseResponseBody<Void>> singIn() {
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/sign-out")
    public ResponseEntity<BaseResponseBody<Void>> signOut(
            @RequestHeader(name = Constant.HEADER_REFRESH_TOKEN) String refreshToken) {
        companyAuthService.signOut(refreshToken);
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/refresh")
    public ResponseEntity<BaseResponseBody<Void>> refresh(
            @RequestHeader(name = Constant.HEADER_REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
        JwtToken jwtToken = companyAuthService.refreshAuth(refreshToken);
        response.setHeader(Constant.HEADER_ACCESS_TOKEN, jwtToken.getAccessToken());
        response.setHeader(Constant.HEADER_REFRESH_TOKEN, jwtToken.getRefreshToken());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/auth-info")
    public ResponseEntity<BaseResponseBody<CompanyAuthInfoResponseDTO>> getCompanyAuthInfo(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo) {
        CompanyAuthInfoResponseDTO companyInfo = companyAuthService.getCompanyAuthInfo(authenticatedUserInfo.id());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(companyInfo));
    }
}
