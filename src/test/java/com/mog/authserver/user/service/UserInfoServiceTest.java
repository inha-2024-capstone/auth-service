package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.UserInfoModifyDTO;
import com.mog.authserver.user.dto.UserInfoSignUpDTO;
import com.mog.authserver.user.exception.UserAlreadyExistException;
import com.mog.authserver.user.exception.UserNotFoundException;
import com.mog.authserver.user.pass.UserInfoPass;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserInfoServiceTest {

    @Autowired
    private UserInfoService userInfoService;

    @Test
    @DisplayName("이메일과 로그인 소스를 통한 UserInfoEntity 반환")
    void findUserInfoByEmailAndLoginSourceTest(){
        // Given
        UserInfoEntity userInfoEntity = new UserInfoEntity("rlwjdd234@naver.com", "kim", "qwer1234!", Role.ADMIN,
                Gender.MALE, "010-1234-5678", "incheon", "whatup", "http://localhost:2020",LoginSource.THIS);
        // When
        UserInfoEntity saveUserInfo = userInfoService.saveUserInfo(userInfoEntity);
        UserInfoEntity userInfoByEmailAndLoginSource = userInfoService.findUserInfoByEmailAndLoginSource(userInfoEntity.getEmail(), userInfoEntity.getLoginSource());
        // Then
        Assertions.assertThat(userInfoByEmailAndLoginSource.getEmail()).isEqualTo(saveUserInfo.getEmail());
        Assertions.assertThat(userInfoByEmailAndLoginSource.getLoginSource()).isEqualTo(saveUserInfo.getLoginSource());
        Assertions.assertThatThrownBy(() -> {
            userInfoService.findUserInfoByEmailAndLoginSource("rlwjddl@naver.com", LoginSource.THIS);
        }).isInstanceOf(UserNotFoundException.class);
        Assertions.assertThatThrownBy(() -> {
            userInfoService.findUserInfoByEmailAndLoginSource("rlwjddl234@naver.com", LoginSource.KAKAO);
        }).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("회원가입 수행, 이메일이 다르거나 로그인 소스가 달라야 함.")
    void signUpTest(){
        // Given
        UserInfoSignUpDTO userInfoEntity1 = new UserInfoSignUpDTO("rlwjddl234@naver.com", "kim", "qwer1234!", Role.ADMIN,
                Gender.MALE, "010-1234-5678", "incheon", "whatup",LoginSource.THIS);

        UserInfoSignUpDTO userInfoEntity2 = new UserInfoSignUpDTO("rlwjddl234@naver.com", "kim", "qwer1234!", Role.ADMIN,
                Gender.MALE, "010-1234-5678", "daegu", "whatup",LoginSource.KAKAO);

        UserInfoSignUpDTO userInfoEntity3 = new UserInfoSignUpDTO("rlwjddl234@naver.com", "kim12", "qwer1234!", Role.ADMIN,
                Gender.MALE, "010-1234-2278", "seoul", "whatup",LoginSource.THIS);
        // When
        UserInfoEntity signUpEntity1 = userInfoService.signUp(userInfoEntity1);
        UserInfoEntity signUpEntity2 = userInfoService.signUp(userInfoEntity2);
        // Then
        Assertions.assertThat(signUpEntity1.getEmail()).isEqualTo(signUpEntity2.getEmail());
        Assertions.assertThat(signUpEntity1.getLoginSource()).isNotEqualTo(signUpEntity2.getLoginSource());
        Assertions.assertThatThrownBy(() -> {
            userInfoService.signUp(userInfoEntity3);
        }).isInstanceOf(UserAlreadyExistException.class);

    }

    @Test
    @DisplayName("id를 통한 UserInfoPass 객체 반환")
    void findUserInfoPassTest(){
        // Given
        UserInfoEntity userInfoEntity = new UserInfoEntity("rlwjdd234@naver.com", "kim", "qwer1234!", Role.ADMIN,
                Gender.MALE, "010-1234-5678", "incheon", "whatup", "http://localhost:2020",LoginSource.THIS);
        UserInfoPass userInfoPass = new UserInfoPass(
                userInfoEntity.getEmail(),
                userInfoEntity.getUsername(),
                userInfoEntity.getRole(),
                userInfoEntity.getGender(),
                userInfoEntity.getPhoneNumber(),
                userInfoEntity.getAddress(),
                userInfoEntity.getNickName(),
                userInfoEntity.getImageUrl(),
                userInfoEntity.getLoginSource()
        );
        // When
        UserInfoEntity saveUserInfo = userInfoService.saveUserInfo(userInfoEntity);
        UserInfoPass findUserInfoPass = userInfoService.findUserInfoPass(saveUserInfo.getId());
        // Then
        Assertions.assertThat(findUserInfoPass).isEqualTo(userInfoPass);
    }

    @Test
    @DisplayName("id와 UserInfoRequestDTO를 통한 OAuth2SignUp 테스트")
    void oAuth2SignUpTest(){
        //given
        UserInfoEntity userInfoEntity = new UserInfoEntity("rlwjddl1596@google.com", null, "qwer12341234!", Role.USER,
                null, null, null, null, "http://localhost:2020",LoginSource.GOOGLE);
        UserInfoModifyDTO userInfoModifyDTO= new UserInfoModifyDTO("rlwjddl1596@google.com", "kim", Role.USER,
                Gender.MALE, "010-1234-5678", "Incheon", "hi", LoginSource.GOOGLE);
        //when
        UserInfoEntity saveUserInfo = userInfoService.saveUserInfo(userInfoEntity);
        UserInfoEntity signUp = userInfoService.modifyUserInfo(userInfoModifyDTO, saveUserInfo.getId());

        //then
        Assertions.assertThat(saveUserInfo).isEqualTo(signUp);
    }

}