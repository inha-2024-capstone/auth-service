package com.mog.authserver.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest  // 모든 빈을 로드
@AutoConfigureMockMvc
class UserInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc를 주입받아 사용

    @Autowired
    private ObjectMapper objectMapper;  // JSON 데이터를 변환할 ObjectMapper

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("sign up test")
    void signUpTest() throws Exception {
        UserInfoRequestDTO userInfoRequestDTO = new UserInfoRequestDTO("rlwjddl234@naver.com",
                "kim",
                "qwer1234567!",
                Role.ADMIN,
                Gender.MALE,
                "010-1234-5678",
                "Incheon",
                "whatup",
                LoginSource.THIS);

        String userJson = objectMapper.writeValueAsString(userInfoRequestDTO);

        mockMvc.perform(post("/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."));

    }

    @Test
    @DisplayName("refresh test")
    void refreshTest() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
        JwtToken jwtToken = jwtService.generateTokenSet(usernamePasswordAuthenticationToken);

        mockMvc.perform(get("/user/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.REFRESH_TOKEN, jwtToken.getRefreshToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(header().exists(Constant.ACCESS_TOKEN))
                .andExpect(header().exists(Constant.REFRESH_TOKEN));

    }
}