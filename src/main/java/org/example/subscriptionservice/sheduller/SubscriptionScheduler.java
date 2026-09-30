package org.example.subscriptionservice.sheduller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.subscriptionservice.entity.Subscription;
import org.example.subscriptionservice.service.SubscriptionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionService subscriptionService;

   //ЗАПУСК КАЖДУЮ МИНУТУ ПОСЛЕ ЗАВЕРШЕНИЯ ПРЕДЫДУЩЕГО


    @Scheduled(
            fixedDelayString = "${app.scheduler.check-expired-subscriptions.fixed-delay-ms:60000}",
            initialDelayString = "${app.scheduler.check-expired-subscriptions.initial-delay-ms:30000}"

    )
    public void checkExpiredSubscriptions() {
        log.debug("Проверка истёкших подписок...");

        List<Subscription> expired = subscriptionService.findExpiredPaidSubscriptions();

        if (expired.isEmpty()) {
            log.debug("Истёкших подписок нет");
            return;
        }

        log.info("Найдено {} истёкших подписок", expired.size());

        for (Subscription sub : expired) {
            try {
                subscriptionService.downgradeToFree(
                        sub.getLogin(),
                        "Subscription expired at " + sub.getExpiresAt()
                );
            } catch (Exception e) {
                // Если одна подписка упала с ошибкой — продолжаем со следующей,
                // чтобы одна проблема не сломала весь цикл
                log.error("Ошибка понижения подписки {}: {}",
                        sub.getLogin(), e.getMessage(), e);
            }
        }
    }
}