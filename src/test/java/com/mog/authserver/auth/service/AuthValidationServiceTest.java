package com.mog.authserver.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.repository.AuthRepository;
import com.mog.authserver.common.config.JpaAuditingConfig;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class AuthValidationServiceTest {

    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthRepository authRepository;

    private AuthPersistService authPersistService;

    private AuthValidationService authValidationService;

    @BeforeEach
    void setUp() {
        authPersistService = new AuthPersistService(authRepository);
        authValidationService = new AuthValidationService(authPersistService, passwordEncoder);
    }

    @ParameterizedTest
    @MethodSource("emailValidationArgs")
    @DisplayName("중복 이메일에 대한 검증 테스트")
    void 중복_이메일_검증(String email, AuthEntity authEntity, boolean expectedBool) {

        //given
        //when
        authRepository.save(authEntity);
        Boolean doesEmailExist = authValidationService.doesEmailExist(email);
        //then
        assertThat(doesEmailExist).isEqualTo(expectedBool);
    }

    private static Stream<Arguments> emailValidationArgs() {
        return Stream.of(
                Arguments.of(
                        "test@test.com",
                        new AuthEntity(
                                null,
                                "test@test.com",
                                "kim",
                                "qwer1234",
                                Role.USER,
                                LoginSource.THIS
                        ),
                        true
                ),
                Arguments.of(
                        "test2@test.com",
                        new AuthEntity(
                                null,
                                "test@test.com",
                                "kim",
                                "qwer1234",
                                Role.USER,
                                LoginSource.THIS
                        ),
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource("pwdValidationArgs")
    @DisplayName("이미 저장돼 있는 비밀번호와 동일한지 테스트")
    void 중복_비밀번호_검증(String pwd, AuthEntity authEntity, boolean expectedBool) {

        //given
        //when
        AuthEntity saved = authRepository.save(authEntity);
        Boolean isPasswordSame = authValidationService.isPasswordSame(saved.getId(), pwd);
        //then
        assertThat(isPasswordSame).isEqualTo(expectedBool);
    }

    private static Stream<Arguments> pwdValidationArgs() {
        return Stream.of(
                Arguments.of(
                        "qwer1234",
                        new AuthEntity(
                                null,
                                "test@test.com",
                                "kim",
                                passwordEncoder.encode("qwer1234"),
                                Role.USER,
                                LoginSource.THIS
                        ),
                        true
                ),
                Arguments.of(
                        "qwer12345",
                        new AuthEntity(
                                null,
                                "test@test2.com",
                                "kim",
                                passwordEncoder.encode( "qwer1234"),
                                Role.USER,
                                LoginSource.THIS
                        ),
                        false
                )
        );
    }
}