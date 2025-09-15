package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Collection;

/**
 * Интерфейс репозитория для работы с дружбой между пользователями.
 * Определяет методы для добавления, удаления и получения друзей.
 */
@Repository
public interface FriendshipRepository {

    /**
     * Добавляет дружбу между пользователями.
     *
     * @param friendship объект дружбы
     */
    void addFriendship(Friendship friendship);

    /**
     * Удаляет дружбу между пользователями.
     *
     * @param userId идентификатор пользователя
     * @param friendId идентификатор друга
     */
    void removeFriendship(Integer userId, Integer friendId);

    /**
     * Возвращает идентификаторы друзей пользователя.
     *
     * @param userId идентификатор пользователя
     * @return коллекция идентификаторов друзей
     */
    Collection<Integer> getUserFriendsIds(Integer userId);

    /**
     * Возвращает идентификаторы общих друзей двух пользователей.
     *
     * @param userId идентификатор первого пользователя
     * @param otherId идентификатор второго пользователя
     * @return коллекция идентификаторов общих друзей
     */
    Collection<Integer> getCommonFriendsIds(Integer userId, Integer otherId);
}