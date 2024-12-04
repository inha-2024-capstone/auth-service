package com.mog.authserver.common;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
public abstract class TestContainer {

    @Container
    private static final MySQLContainer MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER = (MySQLContainer) new MySQLContainer<>("mysql:latest")
                .withDatabaseName("testDB")
                .withUsername("root")
                .withPassword("qwer1234")
                .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");

        MYSQL_CONTAINER.start();
    }

    // 컨테이너 속성을 Spring 환경 속성에 등록
    @DynamicPropertySource
    protected static void setUpProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("hibernate.dialect", () -> "org.hibernate.dialect.MySQL8Dialect");
    }
}