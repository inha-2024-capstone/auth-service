package com.mog.authserver.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.cloud.storage.Storage;
import com.mog.authserver.common.RedisTestContainer;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.request.OauthSignUpRequestDTO;
import com.mog.authserver.user.dto.request.SignUpRequestDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserInfoAuthServiceTest extends RedisTestContainer {

    @Autowired
    private UserInfoAuthService userInfoAuthService;

    @Autowired
    private UserInfoPersistService userInfoPersistService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CacheManager cacheManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private Storage storage;

    @BeforeEach
    void setUp(){
        objectMapper.registerModule(new JavaTimeModule());
    }
    @ParameterizedTest
    @MethodSource("signUpRequestDTO")
    @DisplayName("signUp 매서드 테스트")
    void 회원가입_테스트(SignUpRequestDTO signUpRequestDTO) {
        //given
        //when
        userInfoAuthService.signUp(signUpRequestDTO);
        UserInfoEntity userInfoEntity = userInfoPersistService.findByEmailAndLoginSource(signUpRequestDTO.email(),
                signUpRequestDTO.loginSource());
        //then
        assertThat(signUpRequestDTO.email()).isEqualTo(userInfoEntity.getEmail());
        assertThat(signUpRequestDTO.loginSource()).isEqualTo(userInfoEntity.getLoginSource());
    }

    private static Stream<Arguments> signUpRequestDTO() {
        return Stream.of(Arguments.of(
                new SignUpRequestDTO("example@example.com", "홍길동", "qwer123456!", Role.USER, Gender.MALE,
                        "010-1234-5678", "서울 광역시", "nickname", LoginSource.GOOGLE)));
    }

    @ParameterizedTest
    @MethodSource("oAuthSignUp")
    @DisplayName("oAuthSignUp 매서드 테스트")
    void 회원수정_테스트(SignUpRequestDTO signUpRequestDTO, OauthSignUpRequestDTO oauthSignUpRequestDTO) {
        //given
        //when
        userInfoAuthService.signUp(signUpRequestDTO);
        UserInfoEntity userInfoEntity = userInfoPersistService.findByEmailAndLoginSource(signUpRequestDTO.email(),
                signUpRequestDTO.loginSource());
        String expectedPassword = userInfoEntity.getPassword();
        userInfoAuthService.oAuthSignUp(oauthSignUpRequestDTO, userInfoEntity.getId());
        //then
        assertThat(userInfoEntity.getPassword()).isEqualTo(expectedPassword);
    }

    private static Stream<Arguments> oAuthSignUp() {
        return Stream.of(Arguments.of(
                new SignUpRequestDTO("example@example.com", "홍길동", "qwer123456!", Role.USER, Gender.MALE,
                        "010-1234-5678", "서울 광역시", "nickname", LoginSource.GOOGLE),
                new OauthSignUpRequestDTO(Gender.MALE, "010-1234-5678", "서울 광역시", "imageUri", "nickname")));
    }

    @Test
    void 로그아웃_후_리프레시_테스트() {
        //given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
        //when
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);
        String refreshToken = jwtToken.getRefreshToken();
        userInfoAuthService.signOut(refreshToken);
        //then
        assertThatThrownBy(() -> userInfoAuthService.refreshAuth(refreshToken)).isInstanceOf(RuntimeException.class);
    }
}
