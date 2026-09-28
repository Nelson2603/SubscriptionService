package org.example.subscriptionservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.subscriptionservice.SubscriptionRepository;
import org.example.subscriptionservice.dto.SaveSubscriptionRequest;
import org.example.subscriptionservice.dto.SubscriptionChangedEvent;
import org.example.subscriptionservice.dto.SubscriptionDto;
import org.example.subscriptionservice.entity.Subscription;
import org.example.subscriptionservice.entity.SubscriptionType;
import org.example.subscriptionservice.kafka.KafkaProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    //  бизнес логика буду писать тут что бы работало проект мой

    private final SubscriptionRepository subscriptionRepository;
    private final KafkaProducerService kafkaProducerService;

    public SubscriptionDto getByLogin(String login){
        Subscription sub = subscriptionRepository.findById(login).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ПОДПИСКА НЕ НАЙДЕНА" + login));
        return toDto(sub);
    }


    //СОЗДАНИЕ ИЛИ ОБНОВЛЕНИЕ ПОДПИСКИ

    public SubscriptionDto save(SaveSubscriptionRequest request){
        Subscription sub = Subscription.builder()
                .login(request.login())
                .type(request.type())
                .expiresAt(request.expiresAt())
                .build();
        Subscription saved = subscriptionRepository.save(sub);
        log.info("Подписка сохранена: {} -> {} ",saved.getLogin(), saved.getType());
        return toDto(saved);

    }



    private SubscriptionDto toDto (Subscription s){
        return new SubscriptionDto(
              s.getLogin(),s.getType(),s.getExpiresAt()
        );
    }

    //ПОНИЗИТЬ ПОДПИСКУ А ПОТОМ В КАФКУ
    @Transactional
    public SubscriptionDto downgradeToFree(String login, String reason) {
        Subscription sub = subscriptionRepository.findById(login)

                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Подписка не найдена для логина: " + login
                ));

        sub.setType(SubscriptionType.FREE);
        sub.setExpiresAt(null);
        Subscription saved = subscriptionRepository.save(sub);

        log.info("Подписка понижена до FREE: {} ({})", login, reason);

        // Отправляем событие в Kafka, чтобы FlowManager удалил запись из Redis
        kafkaProducerService.sendSubscriptionChanged(
                new SubscriptionChangedEvent(login, SubscriptionType.FREE, reason)
        );

        return toDto(saved);
    }

   //НАЙТИ ВСЕ ИСТЕКШИЕ СОБЫТИЯ
    public List<Subscription> findExpiredPaidSubscriptions() {
        return subscriptionRepository.findByTypeAndExpiresAtBefore(
                SubscriptionType.PAID,
                LocalDateTime.now()
        );
    }
}
