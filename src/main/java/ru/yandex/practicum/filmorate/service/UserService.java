package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс сервиса для работы с пользователями.
 * Определяет методы для создания, обновления, получения и удаления пользователей,
 * а также для работы с дружбой.
 */
public interface UserService {

    /**
     * Создает нового пользователя.
     *
     * @param user объект пользователя для создания
     * @return созданный пользователь с присвоенным ID
     * @throws ValidationException если пользователь не прошел валидацию
     */
    User createUser(User user) throws ValidationException;

    /**
     * Обновляет существующего пользователя.
     *
     * @param user объект пользователя с обновленными данными
     * @return обновленный пользователь
     * @throws ValidationException если пользователь не найден или не прошел валидацию
     */
    User updateUser(User user) throws ValidationException;

    /**
     * Возвращает коллекцию всех пользователей.
     *
     * @return коллекция всех пользователей
     */
    Collection<User> getAllUsers();

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return Optional с пользователем, если найден, иначе пустой Optional
     */
    Optional<User> getUserById(Integer id);

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя для удаления
     * @return true, если пользователь был удален, иначе false
     */
    boolean deleteUser(Integer id);

    /**
     * Добавляет пользователя в друзья.
     *
     * @param userId идентификатор пользователя
     * @param friendId идентификатор друга
     */
    void addFriend(Integer userId, Integer friendId);

    /**
     * Удаляет пользователя из друзей.
     *
     * @param userId идентификатор пользователя
     * @param friendId идентификатор друга
     */
    void removeFriend(Integer userId, Integer friendId);

    /**
     * Возвращает список друзей пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список друзей
     */
    Collection<User> getUserFriends(Integer userId);

    /**
     * Возвращает список общих друзей двух пользователей.
     *
     * @param userId идентификатор первого пользователя
     * @param otherId идентификатор второго пользователя
     * @return список общих друзей
     */
    Collection<User> getCommonFriends(Integer userId, Integer otherId);
}