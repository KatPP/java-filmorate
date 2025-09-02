package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

/**
 * Модель пользователя.
 * Содержит информацию о пользователе: идентификатор, email, логин, имя и дату рождения.
 */
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
}