package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    // Создание пользователя
    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос на создание пользователя: {}", user.getLogin());

        validateUser(user);

        // Если имя пустое, используем логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан с id: {}", user.getId());
        return user;
    }

    // Обновление пользователя
    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос на обновление пользователя с id: {}", user.getId());

        // Проверка ID
        if (user.getId() == null) {
            log.error("Ошибка обновления пользователя: Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        // Проверка существования пользователя
        if (!users.containsKey(user.getId())) {
            log.error("Ошибка обновления пользователя: Пользователь с id = {} не найден", user.getId());
            throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
        }

        // Валидация
        validateUser(user);

        // Если имя пустое, используем логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        // Обновляем пользователя в хранилище
        users.put(user.getId(), user);
        log.info("Пользователь с id {} успешно обновлен", user.getId());
        return user;
    }

    // Получение всех пользователей
    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей. Количество пользователей: {}", users.size());
        return users.values();
    }

    // Валидация пользователя
    private void validateUser(User user) throws ValidationException {
        // Проверка email
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Валидация не пройдена: Email не может быть пустым");
            throw new ValidationException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Валидация не пройдена: Email должен содержать символ @");
            throw new ValidationException("Email должен содержать символ @");
        }

        // Проверка логина
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Валидация не пройдена: Логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Валидация не пройдена: Логин не должен содержать пробелы");
            throw new ValidationException("Логин не должен содержать пробелы");
        }

        // Проверка даты рождения
        if (user.getBirthday() == null) {
            log.warn("Валидация не пройдена: Дата рождения должна быть указана");
            throw new ValidationException("Дата рождения должна быть указана");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Валидация не пройдена: Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    // Метод для генерации следующего ID
    private int getNextId() {
        return users.keySet()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }
}