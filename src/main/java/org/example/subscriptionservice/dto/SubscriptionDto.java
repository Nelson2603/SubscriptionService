package org.example.subscriptionservice.dto;

import org.example.subscriptionservice.entity.SubscriptionType;

import java.time.LocalDateTime;
 //дто для контроллера - пользователю
public record SubscriptionDto(
        String login,
        SubscriptionType type,
        LocalDateTime expiresAt
) {
}