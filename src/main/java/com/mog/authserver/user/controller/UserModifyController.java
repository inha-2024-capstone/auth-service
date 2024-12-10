package com.mog.authserver.user.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.dto.request.EmailRequestDTO;
import com.mog.authserver.user.dto.request.NickNameRequestDTO;
import com.mog.authserver.user.dto.request.PasswordRequestDTO;
import com.mog.authserver.user.service.UserInfoModifyService;
import com.mog.authserver.user.service.UserInfoValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserModifyController {
    private final UserInfoModifyService userInfoModifyService;
    private final UserInfoValidationService userInfoValidationService;

    @PatchMapping("/password")
    public ResponseEntity<BaseResponseBody<Void>> modifyPassword(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody PasswordRequestDTO passwordRequestDTO) {
        userInfoModifyService.modifyPassword(authenticatedUserInfo.id(), passwordRequestDTO.password());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @PostMapping("/password")
    public ResponseEntity<BaseResponseBody<Boolean>> validatePassword(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody PasswordRequestDTO passwordRequestDTO) {
        Boolean isSamePassword = userInfoValidationService.isSamePassword(authenticatedUserInfo.id(),
                passwordRequestDTO.password());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(isSamePassword));
    }

    @PostMapping("/nickname")
    public ResponseEntity<BaseResponseBody<Boolean>> validateNickname(
            @RequestBody NickNameRequestDTO nickNameRequestDTO) {
        Boolean isSamePassword = userInfoValidationService.doesNickNameExist(nickNameRequestDTO.nickName());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(isSamePassword));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<BaseResponseBody<Void>> modifyNickname(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody NickNameRequestDTO nickNameRequestDTO) {
        userInfoModifyService.modifyNickname(authenticatedUserInfo.id(), nickNameRequestDTO.nickName());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @PostMapping("/email")
    public ResponseEntity<BaseResponseBody<Boolean>> validateEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        Boolean isSameEmail = userInfoValidationService.doesEmailExist(emailRequestDTO.email());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(isSameEmail));
    }
}
