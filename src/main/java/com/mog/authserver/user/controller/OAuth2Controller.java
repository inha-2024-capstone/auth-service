package com.mog.authserver.user.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.dto.UserInfoResponseDTO;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import com.mog.authserver.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/oauth")
@RestController
public class OAuth2Controller {
    private final UserInfoService userInfoService;

    @GetMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<UserInfoResponseDTO>> getUserInfo(Authentication authentication){
        UserInfoEntity userInfoEntity = userInfoService.findUserInfoById(getIdFromAuthentication(authentication));
        if(userInfoEntity.getLoginSource() == LoginSource.THIS) throw new RuntimeException("해당 사용자는 OAuth2.0 사용자가 아닙니다.");
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(UserInfoEntityMapper.toUserInfoResponseDTO(userInfoEntity)));
    }

    public Long getIdFromAuthentication(Authentication authentication) {
        return ((AuthenticatedUserInfo) authentication.getPrincipal()).id();
    }
}
