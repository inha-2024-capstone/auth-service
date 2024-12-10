package com.mog.authserver.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;
import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.request.OauthSignUpRequestDTO;
import com.mog.authserver.user.dto.response.UserInfoResponseDTO;
import com.mog.authserver.user.service.UserInfoPersistService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import org.springframework.boot.test.mock.mockito.MockBean;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OAuth2ControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoPersistService userInfoPersistService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private Storage storage;


    @BeforeEach
    public void setup() {
        // security 적용
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    private static Stream<Arguments> signUpDTOParams() {
        return Stream.of(Arguments.of(
                new UserInfoEntity("test@google.com", "홍길동", "qwer1234567!", Role.USER, null, null, null, null,
                        "http://localhost:2020", LoginSource.GOOGLE)));
    }

    @ParameterizedTest
    @MethodSource("signUpDTOParams")
    @DisplayName("GET /oauth/sign-up 테스트: 회원가입을 위한 OAuth2.0 사용자의 회원정보를 가져옴")
    public void oAuthSignUpTestGet(UserInfoEntity userInfoEntity) throws Exception {
        userInfoPersistService.save(userInfoEntity);
        Authentication authentication = createAuthentication(userInfoEntity);
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);

        System.out.println(jwtToken.getAccessToken());

        MvcResult mvcResult = mockMvc.perform(
                        get("/api/oauth/sign-up").header(Constant.HEADER_AUTHORIZATION, "Bearer " + jwtToken.getAccessToken()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.isSucceeded").value("true"))
                .andExpect(jsonPath("$.message").value("성공입니다.")).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String responseJson = objectMapper.writeValueAsString(
                SuccessStatus.OK.getBaseResponseBody(UserInfoResponseDTO.from(userInfoEntity)));
        Assertions.assertEquals(responseJson, contentAsString);
    }

    @ParameterizedTest
    @MethodSource("oauthSignupParams")
    @DisplayName("PATCH /oauth/sign-up 테스트: 회원가입을 위한 OAuth2.0 사용자의 회원정보를 수정 후 저장")
    public void oAuthSignUpTestPost(UserInfoEntity userInfoEntity, OauthSignUpRequestDTO oauthSignUpRequestDTO)
            throws Exception {
        UserInfoEntity save = userInfoPersistService.save(userInfoEntity);
        Authentication authentication = createAuthentication(userInfoEntity);
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);

        mockMvc.perform(
                        patch("/api/oauth/sign-up").header(Constant.HEADER_AUTHORIZATION, "Bearer " + jwtToken.getAccessToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(oauthSignUpRequestDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.isSucceeded").value("true")).andExpect(jsonPath("$.message").value("성공입니다."))
                .andReturn();

        UserInfoEntity byId = userInfoPersistService.findById(save.getId());
        assertThat(byId.getGender()).isEqualTo(oauthSignUpRequestDTO.gender());
        assertThat(byId.getPhoneNumber()).isEqualTo(oauthSignUpRequestDTO.phoneNumber());
        assertThat(byId.getAddress()).isEqualTo(oauthSignUpRequestDTO.address());
        assertThat(byId.getNickName()).isEqualTo(oauthSignUpRequestDTO.nickName());
    }

    private static Stream<Arguments> oauthSignupParams() {
        return Stream.of(Arguments.of(
                new UserInfoEntity("test@google.com", "홍길동", "qwer1234567!", Role.USER, null, null, null, null,
                        "http://localhost:2020", LoginSource.GOOGLE),
                new OauthSignUpRequestDTO(Gender.MALE, "010-1234-5678", "인천 광역시", "http://localhost:2020",
                        "nickname")));
    }


    private Authentication createAuthentication(UserInfoEntity userInfoEntity) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(userInfoEntity.getId(),
                userInfoEntity.getNickName(), authorities);
        return new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
    }

}