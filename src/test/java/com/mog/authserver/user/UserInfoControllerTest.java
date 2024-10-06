package com.mog.authserver.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.config.SecurityConfig;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest  // 모든 빈을 로드
@AutoConfigureMockMvc
@Transactional
@Import(SecurityConfig.class)
class UserInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc를 주입받아 사용

    @Autowired
    private ObjectMapper objectMapper;  // JSON 데이터를 변환할 ObjectMapper

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        MockMvc mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


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
                        .header(Constant.HEADER_REFRESH_TOKEN, jwtToken.getRefreshToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(header().exists(Constant.HEADER_ACCESS_TOKEN))
                .andExpect(header().exists(Constant.HEADER_REFRESH_TOKEN));

    }

    @Test
    @DisplayName("login test")
    void loginTest() throws Exception {
        UserInfoRequestDTO userInfoRequestDTO = new UserInfoRequestDTO("rlwjddl234@naver.com",
                "kim",
                "qwer1234567!",
                Role.ADMIN,
                Gender.MALE,
                "010-1234-5678",
                "Incheon",
                "whatup",
                LoginSource.THIS);

        userInfoService.signUp(userInfoRequestDTO);

        String authorization = Base64.getEncoder().encodeToString((userInfoRequestDTO.email() + ":" + userInfoRequestDTO.password()).getBytes());
        mockMvc.perform(get("/user/sign-in")
                        .header(Constant.HEADER_AUTHORIZATION, "Basic " + authorization))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(header().exists(Constant.HEADER_ACCESS_TOKEN));

    }
}