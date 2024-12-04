package com.mog.authserver.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mog.authserver.common.TestContainer;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.request.OauthSignUpRequestDTO;
import com.mog.authserver.user.dto.request.SignUpRequestDTO;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserInfoServiceTest extends TestContainer {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoPersistService userInfoPersistService;

    @ParameterizedTest
    @MethodSource("signUpRequestDTO")
    @DisplayName("signUp 매서드 테스트")
    void 회원가입_테스트(SignUpRequestDTO signUpRequestDTO) {
        //given
        //when
        userInfoService.signUp(signUpRequestDTO);
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
    @DisplayName("modifyUserInfo 매서드 테스트")
    void 회원수정_테스트(SignUpRequestDTO signUpRequestDTO, OauthSignUpRequestDTO oauthSignUpRequestDTO) {
        //given
        //when
        userInfoService.signUp(signUpRequestDTO);
        UserInfoEntity userInfoEntity = userInfoPersistService.findByEmailAndLoginSource(signUpRequestDTO.email(),
                signUpRequestDTO.loginSource());
        String expectedPassword = userInfoEntity.getPassword();
        userInfoService.oAuthSignUp(oauthSignUpRequestDTO, userInfoEntity.getId());
        //then
        assertThat(userInfoEntity.getPassword()).isEqualTo(expectedPassword);
    }

    private static Stream<Arguments> oAuthSignUp() {
        return Stream.of(Arguments.of(
                new SignUpRequestDTO("example@example.com", "홍길동", "qwer123456!", Role.USER, Gender.MALE,
                        "010-1234-5678", "서울 광역시", "nickname", LoginSource.GOOGLE),
                new OauthSignUpRequestDTO(Gender.MALE, "010-1234-5678", "서울 광역시", "imageUri", "nickname")));
    }
}
