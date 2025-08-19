package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

/**
 * Модель пользователя.
 * Содержит информацию о пользователе: идентификатор, email, логин, имя и дату рождения.
 */
@Data
@FieldDefaults(level = PRIVATE)
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    Integer id;

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
}