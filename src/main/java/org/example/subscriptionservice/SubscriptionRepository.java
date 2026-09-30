package org.example.subscriptionservice;

import org.example.subscriptionservice.entity.Subscription;
import org.example.subscriptionservice.entity.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,String> {


     //МЕТОД ДЛЯ ШЕДУЛЕРА ЧТО БЫ НАЙТИ ВСЕ ПРОСРОЧЕННЫЕ ПОДПИСКИ И СДЕЛАТЬ FREE
    List<Subscription> findByTypeAndExpiresAtBefore(SubscriptionType type, LocalDateTime now);
}
