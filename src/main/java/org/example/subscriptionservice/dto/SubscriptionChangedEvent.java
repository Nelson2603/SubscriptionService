package org.example.subscriptionservice.dto;


import org.example.subscriptionservice.entity.SubscriptionType;

/**
 * Событие, которое отправляем в Kafka, когда меняется подписка.
 * Например, при понижении PAID -> FREE.
 *
 * FlowManager получит это событие и удалит запись из Redis,
 * чтобы кеш не отдавал устаревшие данные.
 */
public record SubscriptionChangedEvent(
        String login,
        SubscriptionType newType,
        String reason
) {
}