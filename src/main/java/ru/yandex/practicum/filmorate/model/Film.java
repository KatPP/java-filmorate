package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

/**
 * Модель фильма.
 * Содержит информацию о фильме: идентификатор, название, описание, дату релиза и продолжительность.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
public class Film extends BaseEntity {
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

    /**
     * Множество ID пользователей, которые поставили лайк фильму.
     */
    Set<Integer> likes = new HashSet<>();

    /**
     * Жанры фильма
     */
    Set<Genre> genres = new HashSet<>();

    /**
     * Рейтинг MPA
     */
    MpaRating mpa;
}