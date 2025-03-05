package com.mog.authserver.user.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.dto.request.ImageModifyRequestDTO;
import com.mog.authserver.user.dto.request.UserNicknameRequestDTO;
import com.mog.authserver.user.service.UserInfoModifyService;
import com.mog.authserver.user.service.UserInfoValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/nickname")
    public ResponseEntity<BaseResponseBody<Boolean>> validateNickname(
            @RequestBody UserNicknameRequestDTO userNickNameRequestDTO) {
        Boolean isSamePassword = userInfoValidationService.doesNickNameExist(userNickNameRequestDTO.nickName());
        return SuccessStatus.OK.getResponseBody(isSamePassword);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<BaseResponseBody<Void>> modifyNickname(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody UserNicknameRequestDTO userNickNameRequestDTO) {
        userInfoModifyService.modifyNickname(authenticatedUserInfo.id(), userNickNameRequestDTO.nickName());
        return SuccessStatus.OK.getResponseBody();
    }

    @PatchMapping("/image")
    public ResponseEntity<BaseResponseBody<Void>> modifyImage(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody ImageModifyRequestDTO imageModifyRequestDTO) {
        userInfoModifyService.updateProfileImage(authenticatedUserInfo.id(), imageModifyRequestDTO.image());
        return SuccessStatus.OK.getResponseBody();
    }

    @DeleteMapping("/image")
    public ResponseEntity<BaseResponseBody<Void>> deleteImage(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo) {
        userInfoModifyService.deleteAndUpdateDefaultImage(authenticatedUserInfo.id());
        return SuccessStatus.OK.getResponseBody();
    }
}
