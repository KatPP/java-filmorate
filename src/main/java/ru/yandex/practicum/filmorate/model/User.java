package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
public class User extends BaseEntity {
    /**
     * Электронная почта пользователя.
     */
    String email;

    /**
     * Логин пользователя.
     */
    String login;

    /**
     * Имя пользователя для отображения.
     */
    String name;

    /**
     * Дата рождения пользователя.
     */
    LocalDate birthday;

    /**
     * Множество ID друзей пользователя.
     */
    Set<Integer> friends = new HashSet<>();

    /**
     * Статус дружбы с каждым другом: ключ - ID друга, значение - статус дружбы
     */
    Map<Integer, FriendshipStatus> friendsStatus = new HashMap<>();
}