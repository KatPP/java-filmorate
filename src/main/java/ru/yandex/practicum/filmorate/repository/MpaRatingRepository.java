package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с рейтингами MPA.
 * Определяет методы для получения рейтингов из хранилища.
 */
@Repository
public interface MpaRatingRepository {

    /**
     * Возвращает коллекцию всех рейтингов MPA.
     *
     * @return коллекция всех рейтингов MPA
     */
    Collection<MpaRating> findAll();

    /**
     * Находит рейтинг MPA по его идентификатору.
     *
     * @param id идентификатор рейтинга MPA
     * @return Optional с рейтингом MPA, если найден, иначе пустой Optional
     */
    Optional<MpaRating> findById(Integer id);
}