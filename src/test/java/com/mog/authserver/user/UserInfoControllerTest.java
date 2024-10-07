package com.mog.authserver.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.config.SecurityConfig;
import com.mog.authserver.security.firstparty.filter.JwtValidationFilter;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.dto.UserInfoResponseDTO;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import com.mog.authserver.user.service.UserInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
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
@Import({SecurityConfig.class, JwtValidationFilter.class})
class UserInfoControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private UserInfoRequestDTO userInfoRequestDTO;

    private UserInfoResponseDTO userInfoResponseDTO;

    @BeforeEach
    public void setup() {
        // RequestDTO 생성
        userInfoRequestDTO = new UserInfoRequestDTO("rlwjddl234@naver.com",
                "kim",
                "qwer1234567!",
                Role.ADMIN,
                Gender.MALE,
                "010-1234-5678",
                "Incheon",
                "whatup",
                LoginSource.THIS);

        // security 적용
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    @DisplayName("sign up test")
    void signUpTest() throws Exception {

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
        UserInfoEntity signUp = userInfoService.signUp(userInfoRequestDTO);
        JwtToken jwtToken = jwtService.generateTokenSet(createAuthentication(signUp));

        mockMvc.perform(get("/user/refresh")
                        .header(Constant.HEADER_REFRESH_TOKEN, jwtToken.getRefreshToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(header().exists(Constant.HEADER_ACCESS_TOKEN))
                .andExpect(header().exists(Constant.HEADER_REFRESH_TOKEN));

    }

    @Test
    @DisplayName("sign in test")
    void loginTest() throws Exception {
        userInfoService.signUp(userInfoRequestDTO);
        // 자격증명을 전달
        String authorization = Base64.getEncoder().encodeToString((userInfoRequestDTO.email() + ":" + userInfoRequestDTO.password()).getBytes());
        // access token, refresh token을 받아야함.
        mockMvc.perform(get("/user/sign-in")
                        .header(Constant.HEADER_AUTHORIZATION, "Basic " + authorization))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(header().exists(Constant.HEADER_ACCESS_TOKEN))
                .andExpect(header().exists(Constant.HEADER_REFRESH_TOKEN));
    }

    @Test
    @DisplayName("jwt validation test")
    void jwtValidationTest() throws Exception {
        UserInfoEntity signUp = userInfoService.signUp(userInfoRequestDTO);
        JwtToken jwtToken = jwtService.generateTokenSet(createAuthentication(signUp));

        mockMvc.perform(get("/user/test")
                        .header(Constant.HEADER_AUTHORIZATION, "Bearer " + jwtToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."));

    }

    @Test
    @DisplayName("get user info test")
    void getUserTest() throws Exception {
        UserInfoEntity signUp = userInfoService.signUp(userInfoRequestDTO);
        Authentication authentication = createAuthentication(signUp);
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);

        MvcResult mvcResult = mockMvc.perform(get("/user/info")
                        .header(Constant.HEADER_AUTHORIZATION, "Bearer " + jwtToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        userInfoResponseDTO = UserInfoEntityMapper.toUserInfoResponseDTO(signUp);
        String responseDtoToJson = objectMapper.writeValueAsString(SuccessStatus.OK.getBaseResponseBody(userInfoResponseDTO));
        Assertions.assertEquals(contentAsString, responseDtoToJson);

    }

    private Authentication createAuthentication(UserInfoEntity userInfoEntity){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(userInfoEntity.getId(), userInfoEntity.getNickName(), authorities);
        return new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
    }
}