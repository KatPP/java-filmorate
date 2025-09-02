package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    User update(User user);

    Collection<User> findAll();

    Optional<User> findById(Integer id);

    boolean existsById(Integer id);

    boolean deleteById(Integer id);
}