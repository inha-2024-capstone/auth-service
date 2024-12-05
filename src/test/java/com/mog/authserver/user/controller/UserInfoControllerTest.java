package com.mog.authserver.user.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.mog.authserver.user.dto.request.SignUpRequestDTO;
import com.mog.authserver.user.dto.response.UserInfoResponseDTO;
import com.mog.authserver.user.pass.UserInfoPass;
import com.mog.authserver.user.service.UserInfoPersistService;
import com.mog.authserver.user.service.UserInfoService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest  // 모든 빈을 로드
@AutoConfigureMockMvc
@Transactional
class UserInfoControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoPersistService userInfoPersistService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        // security 적용
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @ParameterizedTest
    @MethodSource("signUpParams")
    @DisplayName("/user/sign-up API 테스트: 회원가입")
    void signUpTest(SignUpRequestDTO signUpRequestDTO) throws Exception {

        String userJson = objectMapper.writeValueAsString(signUpRequestDTO);

        mockMvc.perform(post("/api/user/sign-up").contentType(MediaType.APPLICATION_JSON).content(userJson)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true")).andExpect(jsonPath("$.message").value("성공입니다."));
    }

    private static Stream<Arguments> signUpParams() {
        return Stream.of(Arguments.of(
                new SignUpRequestDTO("example@example.com", "홍길동", "qwer123456!", Role.USER, Gender.MALE,
                        "010-1234-5678", "서울 광역시", "nickname", LoginSource.THIS)));
    }

    @ParameterizedTest
    @MethodSource("saveUserInfoEntity")
    @DisplayName("/user/refresh 테스트: 리프레시 토큰을 통한 토큰셋 재발급")
    void refreshTest(UserInfoEntity userInfoEntity) throws Exception {
        UserInfoEntity saved = userInfoPersistService.save(userInfoEntity);
        JwtToken jwtToken = jwtService.generateTokenSet(createAuthentication(saved));

        mockMvc.perform(get("/api/user/refresh").header(Constant.HEADER_REFRESH_TOKEN, jwtToken.getRefreshToken()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(header().exists(Constant.HEADER_ACCESS_TOKEN))
                .andExpect(header().exists(Constant.HEADER_REFRESH_TOKEN));

    }

    private static Stream<Arguments> saveUserInfoEntity() {
        return Stream.of(Arguments.of(
                new UserInfoEntity("test@google.com", "홍길동", "qwer1234567!", Role.USER, null, null, null, null,
                        "http://localhost:2020", LoginSource.THIS)));
    }

    @ParameterizedTest
    @MethodSource("signUpParams")
    @DisplayName("/user/sign-in test: 로그인")
    void signInTest(SignUpRequestDTO signUpRequestDTO) throws Exception {
        userInfoService.signUp(signUpRequestDTO);
        // 자격증명을 전달
        String authorization = Base64.getEncoder()
                .encodeToString((signUpRequestDTO.email() + ":" + signUpRequestDTO.password()).getBytes());
        // access token, refresh token을 받아야함.
        mockMvc.perform(get("/api/user/sign-in").header(Constant.HEADER_AUTHORIZATION, "Basic " + authorization))
                .andExpect(status().isOk()).andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(header().exists(Constant.HEADER_ACCESS_TOKEN))
                .andExpect(header().exists(Constant.HEADER_REFRESH_TOKEN));
    }

    @ParameterizedTest
    @MethodSource("saveUserInfoEntity")
    @DisplayName("/api/user/info 테스트: 엑세스 토큰을 통한 유저정보 반환")
    void userInfoTest(UserInfoEntity userInfoEntity) throws Exception {
        UserInfoEntity save = userInfoPersistService.save(userInfoEntity);
        Authentication authentication = createAuthentication(save);
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);

        MvcResult mvcResult = mockMvc.perform(
                        get("/api/user/info").header(Constant.HEADER_AUTHORIZATION, "Bearer " + jwtToken.getAccessToken()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다.")).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        UserInfoResponseDTO userInfoResponseDTO = UserInfoResponseDTO.from(save);
        String responseDtoToJson = objectMapper.writeValueAsString(
                SuccessStatus.OK.getBaseResponseBody(userInfoResponseDTO));
        Assertions.assertEquals(contentAsString, responseDtoToJson);

    }

    @ParameterizedTest
    @MethodSource("saveUserInfoEntity")
    @DisplayName("/user/pass-id 테스트: 엑세스 토큰을 통한 UserInfoPass 반환")
    void passIdTest(UserInfoEntity userInfoEntity) throws Exception {
        UserInfoEntity save = userInfoPersistService.save(userInfoEntity);
        Authentication authentication = createAuthentication(save);
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);

        mockMvc.perform(
                        get("/api/user/pass-id").header(Constant.HEADER_AUTHORIZATION, "Bearer " + jwtToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(header().string(Constant.HEADER_USER_ID, String.valueOf(save.getId())))
                .andExpect(jsonPath("$.isSucceeded").value("true")).andExpect(jsonPath("$.message").value("성공입니다."));

    }

    @ParameterizedTest
    @MethodSource("saveUserInfoEntity")
    @DisplayName("/user/pass-info/{id} 테스트: 사용자 id를 통한 UserInfoPass 객체 반환")
    void userInfoPassTest(UserInfoEntity userInfoEntity) throws Exception {
        UserInfoEntity save = userInfoPersistService.save(userInfoEntity);
        Authentication authentication = createAuthentication(save);
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);

        UserInfoPass userInfoPass = UserInfoPass.from(save);
        String passToJson = objectMapper.writeValueAsString(SuccessStatus.OK.getBaseResponseBody(userInfoPass));

        MvcResult mvcResult = mockMvc.perform(
                        get("/api/user/pass-info/" + save.getId()).header(Constant.HEADER_AUTHORIZATION,
                                "Bearer " + jwtToken.getAccessToken())).andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true")).andExpect(jsonPath("$.message").value("성공입니다."))
                .andReturn();

        Assertions.assertEquals(passToJson, mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));

    }

    private Authentication createAuthentication(UserInfoEntity userInfoEntity) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(userInfoEntity.getId(),
                userInfoEntity.getNickName(), authorities);
        return new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
    }
}