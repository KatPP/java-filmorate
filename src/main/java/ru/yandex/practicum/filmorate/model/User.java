package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

/**
 * Модель пользователя.
 * Содержит информацию о пользователе: идентификатор, email, логин, имя и дату рождения.
 */
@Data
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    private Integer id;

    /**
     * Электронная почта пользователя.
     */
    private String email;

    /**
     * Логин пользователя.
     */
    private String login;

    /**
     * Имя пользователя для отображения.
     */
    private String name;

    /**
     * Дата рождения пользователя.
     */
    private LocalDate birthday;
}
