package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.UserInfoRequestDTO;
import com.mog.authserver.user.exception.UserAlreadyExistException;
import com.mog.authserver.user.exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserInfoServiceTest {

    @Autowired
    private UserInfoService userInfoService;

    @Test
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
    void signUpTest(){
        // Given
        UserInfoRequestDTO userInfoEntity1 = new UserInfoRequestDTO("rlwjddl234@naver.com", "kim", "qwer1234!", Role.ADMIN,
                Gender.MALE, "010-1234-5678", "incheon", "whatup",LoginSource.THIS);

        UserInfoRequestDTO userInfoEntity2 = new UserInfoRequestDTO("rlwjddl234@naver.com", "kim", "qwer1234!", Role.ADMIN,
                Gender.MALE, "010-1234-5678", "daegu", "whatup",LoginSource.KAKAO);

        UserInfoRequestDTO userInfoEntity3 = new UserInfoRequestDTO("rlwjddl234@naver.com", "kim12", "qwer1234!", Role.ADMIN,
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

}