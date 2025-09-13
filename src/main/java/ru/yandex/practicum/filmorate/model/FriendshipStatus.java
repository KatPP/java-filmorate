package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * Перечисление статусов дружбы между пользователями.
 * Определяет текущее состояние связи дружбы.
 */
@Getter
@AllArgsConstructor
public enum FriendshipStatus {
    /**
     * Неподтверждённая дружба - когда один пользователь отправил запрос
     * на добавление другого пользователя в друзья, но тот ещё не подтвердил.
     */
    PENDING("неподтверждённая"),

    /**
     * Подтверждённая дружба - когда второй пользователь согласился
     * на добавление в друзья.
     */
    CONFIRMED("подтверждённая");

    /**
     * Описание статуса дружбы.
     */
    private final String description;
}