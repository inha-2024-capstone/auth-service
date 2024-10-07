package com.mog.authserver.user.controller;

import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.dto.UserInfoResponseDTO;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import com.mog.authserver.user.pass.UserInfoPass;
import com.mog.authserver.user.service.UserInfoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final JwtService jwtService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<Void>> signUp(@Valid @RequestBody UserInfoRequestDTO userInfoRequestDTO){
        userInfoService.signUp(userInfoRequestDTO);
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/sign-in")
    public ResponseEntity<BaseResponseBody<Void>> signIn(){
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/refresh")
    public ResponseEntity<BaseResponseBody<?>> refresh(@RequestHeader(name = Constant.HEADER_REFRESH_TOKEN) String refreshToken, HttpServletResponse response){
        JwtToken jwtToken = jwtService.reGenerateTokenSet(refreshToken);
        response.setHeader(Constant.HEADER_ACCESS_TOKEN, jwtToken.getAccessToken());
        response.setHeader(Constant.HEADER_REFRESH_TOKEN, jwtToken.getRefreshToken());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/test")
    public ResponseEntity<BaseResponseBody<Void>> testForSecurity(){
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/info")
    public ResponseEntity<BaseResponseBody<UserInfoResponseDTO>> getUserInfo(Authentication authentication){
        AuthenticatedUserInfo authenticatedUserInfo = (AuthenticatedUserInfo) authentication.getPrincipal();
        UserInfoEntity userInfoEntity = userInfoService.findUserInfoById(authenticatedUserInfo.id());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(UserInfoEntityMapper.toUserInfoResponseDTO(userInfoEntity)));
    }

    @GetMapping("/pass-id")
    public ResponseEntity<BaseResponseBody<Void>> passIdToService(HttpServletResponse response, Authentication authentication){
        AuthenticatedUserInfo authenticatedUserInfo = (AuthenticatedUserInfo) authentication.getPrincipal();
        response.setHeader(Constant.HEADER_USER_ID, String.valueOf(authenticatedUserInfo.id()));
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/pass-info/{id}")
    public ResponseEntity<BaseResponseBody<UserInfoPass>> getUserInfoPass(@PathVariable Long id){
        UserInfoPass userInfoPass = userInfoService.findUserInfoPass(id);
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(userInfoPass));
    }
}
