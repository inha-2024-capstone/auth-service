package com.mog.authserver.user;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserInfoController {
    private final UserInfoService userInfoService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<?>> signUp(@Validated @RequestBody UserInfoRequestDTO userInfoRequestDTO){
        userInfoService.signUp(userInfoRequestDTO);
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody());
    }
}
