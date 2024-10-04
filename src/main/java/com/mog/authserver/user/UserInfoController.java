package com.mog.authserver.user;

import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final JwtService jwtService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponseBody<?>> signUp(@Validated @RequestBody UserInfoRequestDTO userInfoRequestDTO){
        userInfoService.signUp(userInfoRequestDTO);
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/sign-in")
    public ResponseEntity<BaseResponseBody<?>> signIn(Authentication authentication){

        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody());
    }

    @GetMapping("/refresh")
    public ResponseEntity<BaseResponseBody<?>> refresh(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = request.getHeader(Constant.HEADER_REFRESH_TOKEN);
        if(refreshToken == null){
            throw new RuntimeException("리프레시 토큰이 존재하지 않습니다.");
        }
        JwtToken jwtToken = jwtService.reGenerateTokenSet(refreshToken);
        response.setHeader(Constant.HEADER_ACCESS_TOKEN, jwtToken.getAccessToken());
        response.setHeader(Constant.HEADER_REFRESH_TOKEN, jwtToken.getRefreshToken());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }
}
