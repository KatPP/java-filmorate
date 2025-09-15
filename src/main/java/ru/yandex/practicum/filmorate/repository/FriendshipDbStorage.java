package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.util.Collection;

/**
 * Реализация хранилища дружбы с использованием JDBC.
 * Отвечает за добавление, удаление и получение друзей в базе данных.
 */
@Repository
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    /**
     * Добавляет дружбу между пользователями.
     *
     * @param friendship объект дружбы
     * @throws NotFoundException если один из пользователей не найден
     */
    @Override
    public void addFriendship(Friendship friendship) {
        if (!userRepository.existsById(friendship.getUserId())) {
            throw new NotFoundException("Пользователь с id = " + friendship.getUserId() + " не найден");
        }
        if (!userRepository.existsById(friendship.getFriendId())) {
            throw new NotFoundException("Пользователь с id = " + friendship.getFriendId() + " не найден");
        }

        String sql = "INSERT INTO friendships (user_id, friend_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getStatus().name());
    }

    /**
     * Удаляет дружбу между пользователями.
     *
     * @param userId идентификатор пользователя
     * @param friendId идентификатор друга
     * @throws NotFoundException если один из пользователей не найден
     */
    @Override
    public void removeFriendship(Integer userId, Integer friendId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (!userRepository.existsById(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }

        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    /**
     * Возвращает идентификаторы друзей пользователя.
     *
     * @param userId идентификатор пользователя
     * @return коллекция идентификаторов друзей
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public Collection<Integer> getUserFriendsIds(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        String sql = "SELECT friend_id FROM friendships WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id"), userId);
    }

    /**
     * Возвращает идентификаторы общих друзей двух пользователей.
     *
     * @param userId идентификатор первого пользователя
     * @param otherId идентификатор второго пользователя
     * @return коллекция идентификаторов общих друзей
     * @throws NotFoundException если один из пользователей не найден
     */
    @Override
    public Collection<Integer> getCommonFriendsIds(Integer userId, Integer otherId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (!userRepository.existsById(otherId)) {
            throw new NotFoundException("Пользователь с id = " + otherId + " не найден");
        }

        String sql = "SELECT f1.friend_id FROM friendships f1 " +
                "JOIN friendships f2 ON f1.friend_id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id"), userId, otherId);
    }
}