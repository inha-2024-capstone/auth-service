package com.mog.authserver.common;


import com.mog.authserver.auth.event.UserUpsertEvent;
import com.mog.authserver.common.KafkaTestConfig.KafkaConsumerConfigValue;
import com.mog.authserver.common.config.KafkaConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@TestConfiguration
@EnableConfigurationProperties({KafkaTestConfig.KafkaProducerConfigValue.class, KafkaConsumerConfigValue.class})
public class KafkaTestConfig {

    private final KafkaProducerConfigValue producerConfigValue;

    private final KafkaConsumerConfigValue consumerConfigValue;


    public KafkaTestConfig(KafkaProducerConfigValue producerConfigValue,
                           KafkaConsumerConfigValue consumerConfigValue) {
        this.producerConfigValue = producerConfigValue;
        this.consumerConfigValue = consumerConfigValue;
    }

    // Admin
    @Bean
    public KafkaAdmin kafkaAdmin() {
        HashMap<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, producerConfigValue.bootstrapServers);

        return new KafkaAdmin(config);
    }

    @Bean
    public KafkaAdmin.NewTopics orderTopics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(KafkaConstant.USER_UPSERT_TOPIC).build()
        );
    }

    // Producer
    @ConfigurationProperties(prefix = "spring.kafka.producer")
    public record KafkaProducerConfigValue(@NotBlank String bootstrapServers, @NotNull Boolean enableIdempotence,
                                           @NotNull String transactionIdPrefix) {
    }

    private HashMap<String, Object> producerConfig() {
        HashMap<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerConfigValue.bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producerConfigValue.enableIdempotence);
        config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, producerConfigValue.transactionIdPrefix);

        return config;
    }

    @Bean
    public KafkaTemplate<String, UserUpsertEvent> defaultKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfig()));
    }

    // Consumer
    @ConfigurationProperties(prefix = "spring.kafka.consumer")
    public record KafkaConsumerConfigValue(
            @NotBlank String bootstrapServers,
            @NotBlank String groupId,
            @NotBlank String autoOffsetReset,
            @NotBlank String enableAutoCommit,
            @NotBlank String isolationLevel) {}


    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfigValue.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfigValue.groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.mog.authserver.auth.event");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerConfigValue.autoOffsetReset);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, consumerConfigValue.isolationLevel);

        return props;
    }

    @Bean
    public Consumer<String, UserUpsertEvent> consumerFactory() {
        Consumer<String, UserUpsertEvent> consumer = new DefaultKafkaConsumerFactory<String, UserUpsertEvent>(
                consumerProps()
        ).createConsumer();

        consumer.subscribe(List.of("user-upsert"));
        return consumer;
    }
}