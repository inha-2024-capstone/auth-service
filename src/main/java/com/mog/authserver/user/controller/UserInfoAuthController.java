package com.mog.authserver.user.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.dto.request.UserSignUpRequestDTO;
import com.mog.authserver.user.dto.response.UserInfoResponseDTO;
import com.mog.authserver.user.dto.response.UserPassDTO;
import com.mog.authserver.user.service.UserInfoAuthService;
import com.mog.authserver.user.service.UserInfoQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserInfoAuthController {
    private final UserInfoAuthService userInfoAuthService;
    private final UserInfoQueryService userInfoQueryService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<Void>> signUp(
            @Valid @RequestBody UserSignUpRequestDTO userInfoUserSignUpRequestDTO) {
        userInfoAuthService.signUp(userInfoUserSignUpRequestDTO);
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/auth-info")
    public ResponseEntity<BaseResponseBody<UserInfoResponseDTO>> getUserInfo(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo) {

        UserInfoResponseDTO userInfoResponseDTO = userInfoQueryService.findUserInfoById(authenticatedUserInfo.id());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(userInfoResponseDTO));
    }

    @GetMapping("/pass/{id}")
    public ResponseEntity<BaseResponseBody<UserPassDTO>> getUserPass(
            @PathVariable(name = "id") Long id) {
        UserPassDTO userPass = userInfoQueryService.getUserPass(id);
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(userPass));
    }
}
