package com.mog.authserver.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.cloud.storage.Storage;
import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.dto.request.AuthSignUpRequestDTO;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthRegisterServiceTest extends UsingRedisTest {

    @MockBean
    private Storage storage;

    @Autowired
    private AuthRegisterService authRegisterService;

    @Autowired
    private AuthPersistService authPersistService;

    @ParameterizedTest
    @MethodSource("signUpArguments")
    @DisplayName("로그인 DTO에 대한 AuthEntity 반환 테스트")
    void AUTH_엔티티_반환(AuthSignUpRequestDTO requestDTO, AuthEntity expectedEntity) {

        //given
        //when
        AuthEntity authEntity = authRegisterService.signUp(requestDTO);
        //then
        assertThat(authEntity)
                .usingRecursiveComparison()
                .ignoringFields("password")
                .isEqualTo(expectedEntity);

    }

    @ParameterizedTest
    @MethodSource("signUpArguments")
    @DisplayName("이미 존재하는 계정에 대한 예외 발생 테스트")
    void 중복_이메일_예외_발생(AuthSignUpRequestDTO requestDTO) {

        //given
        //when
        AuthEntity authEntity = authRegisterService.signUp(requestDTO);
        authPersistService.save(authEntity);
        //then
        assertThatThrownBy(() -> authRegisterService.signUp(requestDTO))
                .isInstanceOf(RuntimeException.class);
    }

    private static Stream<Arguments> signUpArguments() {
        return Stream.of(
                Arguments.of(
                        new AuthSignUpRequestDTO(
                                "kim",
                                "test@test.com",
                                "qwer1234",
                                Role.USER,
                                LoginSource.THIS
                        ),
                        new AuthEntity(
                                null,
                                "test@test.com",
                                "kim",
                                "qwer1234",
                                Role.USER,
                                LoginSource.THIS
                        )
                )
        );
    }
}