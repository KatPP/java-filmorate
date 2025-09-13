package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

/**
 * Модель жанра фильма.
 * Определяет категорию или тип фильма.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
public class Genre extends BaseEntity {
    /**
     * Название жанра.
     */
    String name;

    /**
     * Конструктор по умолчанию.
     */
    public Genre() {}

    /**
     * Конструктор с параметрами.
     *
     * @param id идентификатор жанра
     * @param name название жанра
     */
    public Genre(Integer id, String name) {
        this.setId(id);
        this.name = name;
    }

    /**
     * Жанр "Комедия".
     */
    public static final Genre COMEDY = new Genre(1, "Комедия");

    /**
     * Жанр "Драма".
     */
    public static final Genre DRAMA = new Genre(2, "Драма");

    /**
     * Жанр "Мультфильм".
     */
    public static final Genre CARTOON = new Genre(3, "Мультфильм");

    /**
     * Жанр "Триллер".
     */
    public static final Genre THRILLER = new Genre(4, "Триллер");

    /**
     * Жанр "Документальный".
     */
    public static final Genre DOCUMENTARY = new Genre(5, "Документальный");

    /**
     * Жанр "Боевик".
     */
    public static final Genre ACTION = new Genre(6, "Боевик");
}