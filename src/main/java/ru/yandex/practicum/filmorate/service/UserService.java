package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    Collection<User> getAllUsers();

    boolean deleteUser(Integer id);

    Optional<User> getUserById(Integer id);

    // Методы для работы с друзьями
    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    List<User> getUserFriends(Integer userId);

    List<User> getCommonFriends(Integer userId, Integer otherUserId);
}