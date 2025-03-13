package com.mog.authserver.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.cloud.storage.Storage;
import com.mog.authserver.auth.event.UserUpsertEvent;
import com.mog.authserver.common.IntegrationTest;
import com.mog.authserver.common.constant.KafkaConstant;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Gender;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.dto.request.UserSignUpRequestDTO;
import java.time.Duration;
import java.util.stream.Stream;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class UserInfoAuthServiceTest extends IntegrationTest {

    @MockBean
    private Storage storage;

    @Autowired
    KafkaTemplate<String, UserUpsertEvent> kafkaTemplate;

    @Autowired
    Consumer<String, UserUpsertEvent> consumer;

    @Autowired
    private UserInfoAuthService userInfoAuthService;

    @Autowired
    private UserInfoPersistService userInfoPersistService;

    @ParameterizedTest
    @MethodSource("userSignUpArgs")
    @DisplayName("일반 유저 회원가입 테스트")
    void 일반_유저_회원가입_테스트(UserSignUpRequestDTO requestDTO, UserInfoEntity userInfoEntity,
                        UserUpsertEvent userUpsertEvent) {
        //given
        //when
        userInfoAuthService.signUp(requestDTO);
        UserInfoEntity actualUserInfo = userInfoPersistService.findByEmailAndLoginSource(requestDTO.email(),
                requestDTO.loginSource());
        //then
        assertThat(actualUserInfo).usingRecursiveComparison().comparingOnlyFields(
                "gender", "phoneNumber", "address", "nickName", "imageUrl"
        ).isEqualTo(userInfoEntity);

        ConsumerRecords<String, UserUpsertEvent> poll = consumer.poll(Duration.ofSeconds(10));
        poll.records(KafkaConstant.USER_UPSERT_TOPIC).forEach(
                record -> {
                    assertThat(record.value()).usingRecursiveComparison()
                            .comparingOnlyFields("imageUrl")
                            .isEqualTo(userUpsertEvent);
                    assertThat(record.value()).usingRecursiveComparison()
                            .comparingOnlyFields("email", "username", "role")
                            .isEqualTo(userUpsertEvent);
                }
        );
    }

    private static Stream<Arguments> userSignUpArgs() {
        return Stream.of(
                Arguments.of(
                        new UserSignUpRequestDTO(
                                "test@test.com",
                                "test",
                                "qwer1234",
                                Role.USER,
                                Gender.MALE,
                                "010-1234-5678",
                                "asdasd",
                                "test",
                                LoginSource.THIS
                        ),
                        new UserInfoEntity(
                                null,
                                Gender.MALE,
                                "010-1234-5678",
                                "asdasd",
                                "test",
                                "asda",
                                null
                        ),
                        new UserUpsertEvent(
                                null,
                                "test",
                                "test@test.com",
                                "asda",
                                Role.USER,
                                null
                        )
                )
        );
    }

}