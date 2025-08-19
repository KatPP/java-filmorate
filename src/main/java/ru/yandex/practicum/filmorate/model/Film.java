package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

/**
 * Модель фильма.
 * Содержит информацию о фильме: идентификатор, название, описание, дату релиза и продолжительность.
 */
@Data
@FieldDefaults(level = PRIVATE)
public class Film {
    /**
     * Уникальный идентификатор фильма.
     */
    Integer id;

    /**
     * Название фильма.
     */
    String name;

    /**
     * Описание фильма.
     */
    String description;

    /**
     * Дата релиза фильма.
     */
    LocalDate releaseDate;

    /**
     * Продолжительность фильма в минутах.
     */
    Integer duration;
}