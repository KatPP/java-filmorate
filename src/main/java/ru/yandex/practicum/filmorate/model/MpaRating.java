package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

/**
 * Модель рейтинга Ассоциации кинокомпаний (MPA).
 * Определяет возрастное ограничение для фильма.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
public class MpaRating extends BaseEntity {
    /**
     * Название рейтинга MPA.
     */
    String name;

    /**
     * Конструктор по умолчанию.
     */
    public MpaRating() {}

    /**
     * Конструктор с параметрами.
     *
     * @param id идентификатор рейтинга
     * @param name название рейтинга
     */
    public MpaRating(Integer id, String name) {
        this.setId(id);
        this.name = name;
    }

    /**
     * Рейтинг G - у фильма нет возрастных ограничений.
     */
    public static final MpaRating G = new MpaRating(1, "G");

    /**
     * Рейтинг PG - детям рекомендуется смотреть фильм с родителями.
     */
    public static final MpaRating PG = new MpaRating(2, "PG");

    /**
     * Рейтинг PG-13 - детям до 13 лет просмотр не желателен.
     */
    public static final MpaRating PG13 = new MpaRating(3, "PG-13");

    /**
     * Рейтинг R - лицам до 17 лет просматривать фильм можно только в присутствии взрослого.
     */
    public static final MpaRating R = new MpaRating(4, "R");

    /**
     * Рейтинг NC-17 - лицам до 18 лет просмотр запрещён.
     */
    public static final MpaRating NC17 = new MpaRating(5, "NC-17");
}