package com.mog.authserver.auth.producer;

import com.mog.authserver.auth.event.UserUpsertEvent;
import com.mog.authserver.common.constant.KafkaConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserUpsertProducer {
    private final KafkaTemplate<String, UserUpsertEvent> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishUserUpsert(UserUpsertEvent userUpsertEvent) {
        kafkaTemplate
                .send(KafkaConstant.USER_UPSERT_TOPIC,
                        String.valueOf(userUpsertEvent.id()), userUpsertEvent)
                .thenAcceptAsync(result -> {
                    RecordMetadata recordMetadata = result.getRecordMetadata();
                    ProducerRecord<String, UserUpsertEvent> producerRecord = result.getProducerRecord();
                    log.info("[PRODUCER] topic={}, partition={}, offset={}, event={}", recordMetadata.topic(), recordMetadata.partition(),
                            recordMetadata.offset(), producerRecord.value().eventId());})
                .exceptionallyAsync(result -> {
                    throw new RuntimeException(result.getMessage() + "\n 채팅 메시지 전송에 실패했습니다.");
                });
    }
}
