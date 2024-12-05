package com.mog.authserver.user.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.dto.request.OauthSignUpRequestDTO;
import com.mog.authserver.user.dto.response.UserInfoResponseDTO;
import com.mog.authserver.user.service.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final UserInfoService userInfoService;

    @GetMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<UserInfoResponseDTO>> getSignUpInfo(Authentication authentication){
        UserInfoResponseDTO userInfoResponseDTO = userInfoService.findOauth2UserInfoById(getIdFromAuthentication(authentication));
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(userInfoResponseDTO));
    }

    @PatchMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<?>> signUp(@Valid @RequestBody OauthSignUpRequestDTO oauthSignUpRequestDTO, Authentication authentication){
        userInfoService.oAuthSignUp(oauthSignUpRequestDTO, getIdFromAuthentication(authentication));
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    public Long getIdFromAuthentication(Authentication authentication) {
        return ((AuthenticatedUserInfo) authentication.getPrincipal()).id();
    }
}
