package org.example.subscriptionservice.dto;

import org.example.subscriptionservice.entity.SubscriptionType;

import java.time.LocalDateTime;



       //ДЛЯ СОЗДАНИЯ И ОБНОВЛЕНИИ ПОДПИСКИ

public record SaveSubscriptionRequest(
        String login,
        SubscriptionType type,
        LocalDateTime expiresAt
) {
}