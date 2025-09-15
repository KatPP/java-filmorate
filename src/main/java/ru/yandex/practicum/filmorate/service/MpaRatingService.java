package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс сервиса для работы с рейтингами MPA.
 * Определяет методы для получения рейтингов.
 */
public interface MpaRatingService {

    /**
     * Возвращает коллекцию всех рейтингов MPA.
     *
     * @return коллекция всех рейтингов MPA
     */
    Collection<MpaRating> getAllMpaRatings();

    /**
     * Находит рейтинг MPA по его идентификатору.
     *
     * @param id идентификатор рейтинга MPA
     * @return Optional с рейтингом MPA, если найден, иначе пустой Optional
     */
    Optional<MpaRating> getMpaRatingById(Integer id);
}