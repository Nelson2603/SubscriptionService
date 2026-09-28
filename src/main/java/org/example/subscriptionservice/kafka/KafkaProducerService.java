package org.example.subscriptionservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.subscriptionservice.dto.SubscriptionChangedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.subscription-changed}")
    private String topic;

    public void sendSubscriptionChanged(SubscriptionChangedEvent event){
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic,event.login(),json);
            log.info("Событие отправлено в топик {}: {}", topic, json);

        }catch (Exception e) {
            log.error("Ошибка отправки события в Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось отправить событие в Kafka", e);
        }
    }
}
