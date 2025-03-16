package com.mog.authserver.auth.controller;

import com.mog.authserver.auth.dto.request.AuthEmailRequestDTO;
import com.mog.authserver.auth.dto.request.AuthPwdRequestDTO;
import com.mog.authserver.auth.service.AuthModifyService;
import com.mog.authserver.auth.service.AuthValidationService;
import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthInfoController {
    private final AuthValidationService authValidationService;
    private final AuthModifyService authModifyService;

    @PatchMapping("/pwd")
    public ResponseEntity<BaseResponseBody<Void>> changePwd(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody AuthPwdRequestDTO authPwdRequestDTO) {
        authModifyService.changePassword(authenticatedUserInfo.id(), authPwdRequestDTO.pwd());

        return SuccessStatus.OK.getResponseBody();
    }

    @PostMapping("/pwd")
    public ResponseEntity<BaseResponseBody<Boolean>> validatePwd(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody AuthPwdRequestDTO authPwdRequestDTO) {

        Boolean isPwdSame = authValidationService.isPasswordSame(authenticatedUserInfo.id(),
                authPwdRequestDTO.pwd());
        return SuccessStatus.OK.getResponseBody(isPwdSame);
    }

    @PostMapping("/email")
    public ResponseEntity<BaseResponseBody<Boolean>> validateEmail(@RequestBody AuthEmailRequestDTO authEmailRequestDTO) {
        Boolean doesEmailExist = authValidationService.doesEmailExist(authEmailRequestDTO.email());

        return SuccessStatus.OK.getResponseBody(doesEmailExist);
    }
}
