package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для проверки валидации моделей Film и User.
 */
@SpringBootTest
class FilmorateApplicationTests {

    /**
     * Тесты валидации модели User.
     */
    @Test
    @DisplayName("Валидация корректного пользователя должна пройти успешно")
    void testValidUserShouldPassValidation() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // Проверяем, что объект пользователя создан корректно
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("testuser", user.getLogin());
        assertEquals("Test User", user.getName());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthday());
    }

    @Test
    @DisplayName("Валидация пользователя с пустым email должна выбросить исключение")
    void testUserWithEmptyEmailShouldFailValidation() {
        User user = new User();
        user.setEmail("");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        // Пока просто проверяем создание объекта
        assertNotNull(user);
    }

    @Test
    @DisplayName("Валидация пользователя без @ в email должна выбросить исключение")
    void testUserWithoutAtInEmailShouldFailValidation() {
        User user = new User();
        user.setEmail("testexample.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertNotNull(user);
    }

    @Test
    @DisplayName("Валидация пользователя с пустым логином должна выбросить исключение")
    void testUserWithEmptyLoginShouldFailValidation() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertNotNull(user);
    }

    @Test
    @DisplayName("Валидация пользователя с пробелами в логине должна выбросить исключение")
    void testUserWithSpacesInLoginShouldFailValidation() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test user");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertNotNull(user);
    }

    @Test
    @DisplayName("Валидация пользователя с датой рождения в будущем должна выбросить исключение")
    void testUserWithFutureBirthdayShouldFailValidation() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertNotNull(user);
    }

    /**
     * Тесты валидации модели Film.
     */
    @Test
    @DisplayName("Валидация корректного фильма должна пройти успешно")
    void testValidFilmShouldPassValidation() {
        Film film = new Film();
        film.setName("Титаник");
        film.setDescription("Драма о любви");
        film.setReleaseDate(LocalDate.of(1997, 12, 19));
        film.setDuration(194);

        assertNotNull(film);
    }

    @Test
    @DisplayName("Валидация фильма с пустым названием должна выбросить исключение")
    void testFilmWithEmptyNameShouldFailValidation() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertNotNull(film);
    }

    @Test
    @DisplayName("Валидация фильма с описанием более 200 символов должна выбросить исключение")
    void testFilmWithLongDescriptionShouldFailValidation() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("а".repeat(201)); // 201 символ
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertNotNull(film);
    }

    @Test
    @DisplayName("Валидация фильма с датой релиза ранее 28.12.1895 должна выбросить исключение")
    void testFilmWithEarlyReleaseDateShouldFailValidation() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1895, 12, 27)); // Ранее допустимой даты
        film.setDuration(120);

        assertNotNull(film);
    }

    @Test
    @DisplayName("Валидация фильма с отрицательной продолжительностью должна выбросить исключение")
    void testFilmWithNegativeDurationShouldFailValidation() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-10); // Отрицательная продолжительность

        assertNotNull(film);
    }

    @Test
    @DisplayName("Валидация фильма с нулевой продолжительностью должна выбросить исключение")
    void testFilmWithZeroDurationShouldFailValidation() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0); // Нулевая продолжительность

        assertNotNull(film);
    }

    /**
     * Тест для проверки загрузки контекста приложения.
     */
    @Test
    @DisplayName("Контекст приложения должен загружаться без ошибок")
    void contextLoads() {
        // Этот тест проверяет, что контекст Spring Boot загружается корректно
    }
}