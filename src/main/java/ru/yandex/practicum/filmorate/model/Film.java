package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

/**
 * Модель фильма.
 * Содержит информацию о фильме: идентификатор, название, описание, дату релиза и продолжительность.
 */
@Data
public class Film {
    /**
     * Уникальный идентификатор фильма.
     */
    private Integer id;

    /**
     * Название фильма.
     */
    private String name;

    /**
     * Описание фильма.
     */
    private String description;

    /**
     * Дата релиза фильма.
     */
    private LocalDate releaseDate;

    /**
     * Продолжительность фильма в минутах.
     */
    private Integer duration;
}