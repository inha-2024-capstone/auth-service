package com.mog.authserver.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.cloud.storage.Storage;
import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.repository.AuthRepository;
import com.mog.authserver.common.UsingRedisTest;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthModifyServiceTest extends UsingRedisTest {

    @MockBean
    private Storage storage;

    @Autowired
    private AuthModifyService authModifyService;

    @Autowired
    private AuthRepository authRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @ParameterizedTest
    @MethodSource("modifyPwdArgs")
    @DisplayName("비밀번호 변경 테스트")
    void 비밀번호_변경_테스트(String newPwd, AuthEntity authEntity, boolean expectedBool){
        //given
        //when
        AuthEntity saved = authRepository.save(authEntity);
        authModifyService.changePassword(authEntity.getId(), newPwd);
        //then
        assertThat(passwordEncoder.matches(newPwd, saved.getPassword())).isEqualTo(expectedBool);
    }

    private static Stream<Arguments> modifyPwdArgs() {
        return Stream.of(
                Arguments.of(
                        "newpwd",
                        new AuthEntity(
                                null,
                                "test@test.com",
                                "kim",
                                "qwer1234",
                                Role.USER,
                                LoginSource.THIS
                        ),
                        true
                )
        );
    }
}