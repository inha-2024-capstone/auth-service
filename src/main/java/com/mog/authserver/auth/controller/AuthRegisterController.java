package com.mog.authserver.auth.controller;

import com.mog.authserver.auth.service.AuthRegisterService;
import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRegisterController {
    private final AuthRegisterService authRegisterService;

    @GetMapping("/sign-in")
    public ResponseEntity<BaseResponseBody<Void>> signIn() {
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/sign-out")
    public ResponseEntity<BaseResponseBody<Void>> signOut(
            @RequestHeader(name = Constant.HEADER_REFRESH_TOKEN) String refreshToken) {
        authRegisterService.signOut(refreshToken);
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/refresh")
    public ResponseEntity<BaseResponseBody<?>> refresh(
            @RequestHeader(name = Constant.HEADER_REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
        JwtToken jwtToken = authRegisterService.refreshAuth(refreshToken);
        response.setHeader(Constant.HEADER_ACCESS_TOKEN, jwtToken.getAccessToken());
        response.setHeader(Constant.HEADER_REFRESH_TOKEN, jwtToken.getRefreshToken());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/pass-id")
    public ResponseEntity<BaseResponseBody<Void>> passIdToService(HttpServletResponse response,
                                                                  @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo) {
        response.setHeader(Constant.HEADER_USER_ID, String.valueOf(authenticatedUserInfo.id()));
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }
}
