package com.mog.authserver.common;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@Import(KafkaTestConfig.class)
public abstract class IntegrationTest extends UsingRedisTest{

    @Container
    static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @DynamicPropertySource
    public static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        String bootstrapServers = KAFKA_CONTAINER.getBootstrapServers();
        registry.add("spring.kafka.admin.bootstrap-servers", () -> bootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", () -> bootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", () -> bootstrapServers);
    }
}
