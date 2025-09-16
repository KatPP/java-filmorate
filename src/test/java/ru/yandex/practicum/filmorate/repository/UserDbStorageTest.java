package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционные тесты для UserDbStorage.
 * Проверяют работу с базой данных для пользователей.
 */
@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class})
class UserDbStorageTest {

    @Autowired
    private UserDbStorage userStorage;

    /**
     * Тест сохранения пользователя.
     */
    @Test
    void testSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User savedUser = userStorage.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getLogin()).isEqualTo("testuser");
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    /**
     * Тест поиска пользователя по ID.
     */
    @Test
    void testFindUserById() {
        User user = new User();
        user.setEmail("test2@example.com");
        user.setLogin("testuser2");
        user.setName("Test User 2");
        user.setBirthday(LocalDate.of(1995, 5, 5));

        User savedUser = userStorage.save(user);
        Optional<User> foundUser = userStorage.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getEmail()).isEqualTo("test2@example.com");
    }

    /**
     * Тест обновления пользователя.
     */
    @Test
    void testUpdateUser() {
        User user = new User();
        user.setEmail("original@example.com");
        user.setLogin("original");
        user.setName("Original Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User savedUser = userStorage.save(user);

        savedUser.setEmail("updated@example.com");
        savedUser.setName("Updated Name");

        User updatedUser = userStorage.update(savedUser);

        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }

    /**
     * Тест удаления пользователя.
     */
    @Test
    void testDeleteUser() {
        User user = new User();
        user.setEmail("delete@example.com");
        user.setLogin("deleteuser");
        user.setName("Delete User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User savedUser = userStorage.save(user);
        boolean deleted = userStorage.deleteById(savedUser.getId());

        assertThat(deleted).isTrue();
        assertThat(userStorage.findById(savedUser.getId())).isEmpty();
    }
}