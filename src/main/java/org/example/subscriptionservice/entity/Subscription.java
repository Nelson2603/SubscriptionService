package org.example.subscriptionservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {


     // Логин пользователя из Keycloak — первичный ключ.
     // Совпадает с preferred_username в JWT-токене.
    @Id
    @Column(name = "login", nullable = false, unique = true)
    private String login;

        //ТИП ПОДПИСКИ
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private SubscriptionType type;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;  //ДАТА ИСТЕЧЕНИЯ ПОДПИСКИ ЕСЛИ ОНА ЕСТЬ ELSE NULL

    @Column(name = "created_at", nullable = false) //КОГДА СОЗДАНА
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    LocalDateTime updatedAt;

    @PrePersist
    void onCreate(){ //метод автомат при вставке в бд

        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() { //вызывается при изменении в бд
        updatedAt = LocalDateTime.now();
    }
}
