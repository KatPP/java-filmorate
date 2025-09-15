package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;

/**
 * Интерфейс репозитория для работы с лайками фильмов.
 * Определяет методы для добавления, удаления и подсчета лайков.
 */
@Repository
public interface LikeRepository {

    /**
     * Добавляет лайк фильму от пользователя.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    void addLike(Integer filmId, Integer userId);

    /**
     * Удаляет лайк у фильма от пользователя.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    void removeLike(Integer filmId, Integer userId);

    /**
     * Возвращает количество лайков у фильма.
     *
     * @param filmId идентификатор фильма
     * @return количество лайков
     */
    int getLikesCount(Integer filmId);
}