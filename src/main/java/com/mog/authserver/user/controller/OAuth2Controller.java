package com.mog.authserver.user.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.dto.request.OauthSignUpRequestDTO;
import com.mog.authserver.user.dto.response.UserInfoResponseDTO;
import com.mog.authserver.user.service.UserInfoAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/oauth")
@RestController
public class OAuth2Controller {
    private final UserInfoAuthService userInfoAuthService;

    @GetMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<UserInfoResponseDTO>> getSignUpInfo(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo) {
        UserInfoResponseDTO userInfoResponseDTO = userInfoAuthService.findOauth2UserInfoById(
                authenticatedUserInfo.id());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(userInfoResponseDTO));
    }

    @PatchMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<?>> signUp(@Valid @RequestBody OauthSignUpRequestDTO oauthSignUpRequestDTO,
                                                      @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo) {
        userInfoAuthService.oAuthSignUp(oauthSignUpRequestDTO, authenticatedUserInfo.id());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }
}
