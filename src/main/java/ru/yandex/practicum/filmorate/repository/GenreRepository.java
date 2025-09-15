package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с жанрами фильмов.
 * Определяет методы для получения жанров из хранилища.
 */
@Repository
public interface GenreRepository {

    /**
     * Возвращает коллекцию всех жанров.
     *
     * @return коллекция всех жанров
     */
    Collection<Genre> findAll();

    /**
     * Находит жанр по его идентификатору.
     *
     * @param id идентификатор жанра
     * @return Optional с жанром, если найден, иначе пустой Optional
     */
    Optional<Genre> findById(Integer id);
}