package org.example.subscriptionservice.dto;


import org.example.subscriptionservice.entity.SubscriptionType;


public record SubscriptionChangedEvent(
        String login,
        SubscriptionType newType,
        String reason
) {
}