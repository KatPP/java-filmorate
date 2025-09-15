package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс сервиса для работы с жанрами фильмов.
 * Определяет методы для получения жанров.
 */
public interface GenreService {

    /**
     * Возвращает коллекцию всех жанров.
     *
     * @return коллекция всех жанров
     */
    Collection<Genre> getAllGenres();

    /**
     * Находит жанр по его идентификатору.
     *
     * @param id идентификатор жанра
     * @return Optional с жанром, если найден, иначе пустой Optional
     */
    Optional<Genre> getGenreById(Integer id);
}