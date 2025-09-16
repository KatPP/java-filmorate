package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Реализация хранилища лайков с использованием JDBC.
 * Отвечает за добавление, удаление и подсчет лайков в базе данных.
 */
@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Добавляет лайк фильму от пользователя.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    /**
     * Удаляет лайк у фильма от пользователя.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    /**
     * Возвращает количество лайков у фильма.
     *
     * @param filmId идентификатор фильма
     * @return количество лайков
     */
    @Override
    public int getLikesCount(Integer filmId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE film_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, filmId);
        return count != null ? count : 0;
    }
}