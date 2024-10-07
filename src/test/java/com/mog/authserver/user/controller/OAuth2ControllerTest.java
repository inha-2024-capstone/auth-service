package com.mog.authserver.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.mapper.UserInfoEntityMapper;
import com.mog.authserver.user.service.UserInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OAuth2ControllerTest {

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

    private UserInfoEntity userInfoEntity;

    @BeforeEach
    public void setup() {
        // RequestDTO 생성
        userInfoRequestDTO = new UserInfoRequestDTO(
                "rlwjddl1596@google.com",
                "kim",
                "qwer1234567!",
                Role.USER,
                Gender.MALE,
                "010-1234-5678",
                "Incheon",
                "whatup",
                LoginSource.GOOGLE);

        UserInfoEntity newUserInfoEntity = new UserInfoEntity("rlwjddl1596@google.com", "kim", "qwer1234567!", Role.USER,
                null, null, null, null, "http://localhost:2020",LoginSource.GOOGLE);

        this.userInfoEntity = userInfoService.saveUserInfo(newUserInfoEntity);

        // security 적용
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("GET /oauth/sign-up 테스트: 회원가입을 위한 OAuth2.0 사용자의 회원정보를 가져옴")
    public void oAuthSignUpTestGet() throws Exception {
        Authentication authentication = createAuthentication(userInfoEntity);
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);

        MvcResult mvcResult = mockMvc.perform(get("/oauth/sign-up")
                        .header(Constant.HEADER_AUTHORIZATION, "Bearer " + jwtToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String responseJson = objectMapper
                .writeValueAsString(SuccessStatus.OK.getBaseResponseBody(UserInfoEntityMapper.toUserInfoResponseDTO(userInfoEntity)));
        Assertions.assertEquals(responseJson, contentAsString);

    }

    @Test
    @DisplayName("POST /oauth/sign-up 테스트: 회원가입을 위한 OAuth2.0 사용자의 회원정보를 수정 후 저장")
    public void oAuthSignUpTestPost() throws Exception {

        Authentication authentication = createAuthentication(userInfoEntity);
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);

        mockMvc.perform(patch("/oauth/sign-up")
                        .header(Constant.HEADER_AUTHORIZATION, "Bearer " + jwtToken.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andReturn();

        UserInfoEntity userInfoByEmailAndLoginSource = userInfoService.findUserInfoByEmailAndLoginSource(userInfoRequestDTO.email(), userInfoRequestDTO.loginSource());
        Assertions.assertEquals(userInfoByEmailAndLoginSource.getEmail(), userInfoRequestDTO.email());
        Assertions.assertEquals(userInfoByEmailAndLoginSource.getUsername(), userInfoRequestDTO.username());
        Assertions.assertEquals(userInfoByEmailAndLoginSource.getPassword(), userInfoRequestDTO.password());
        Assertions.assertEquals(userInfoByEmailAndLoginSource.getRole(), userInfoRequestDTO.role());
        Assertions.assertEquals(userInfoByEmailAndLoginSource.getGender(), userInfoRequestDTO.gender());
        Assertions.assertEquals(userInfoByEmailAndLoginSource.getAddress(), userInfoRequestDTO.address());
        Assertions.assertEquals(userInfoByEmailAndLoginSource.getPhoneNumber(), userInfoRequestDTO.phoneNumber());
        Assertions.assertEquals(userInfoByEmailAndLoginSource.getLoginSource(), userInfoRequestDTO.loginSource());
        Assertions.assertEquals(userInfoByEmailAndLoginSource.getNickName(), userInfoRequestDTO.nickName());

    }

    private Authentication createAuthentication(UserInfoEntity userInfoEntity){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(userInfoEntity.getId(), userInfoEntity.getNickName(), authorities);
        return new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
    }

}