package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Модель связи дружбы между двумя пользователями.
 * Содержит информацию о пользователях и статусе их дружбы.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friendship {
    /**
     * Идентификатор пользователя, инициировавшего запрос дружбы.
     */
    private Integer userId;

    /**
     * Идентификатор пользователя, которому отправлен запрос дружбы.
     */
    private Integer friendId;

    /**
     * Статус дружбы между пользователями.
     */
    private FriendshipStatus status;
}