package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleUserService implements UserService {

    private final InMemoryUserRepository userRepository;

    @Override
    public User createUser(User user) throws ValidationException {
        log.info("Создание пользователя: {}", user.getLogin());

        validateUser(user);

        // Если имя пустое, используем логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(userRepository.getNextId());
        User savedUser = userRepository.save(user);
        log.info("Пользователь успешно создан с id: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        log.info("Обновление пользователя с id: {}", user.getId());

        // Проверка ID
        if (user.getId() == null) {
            log.error("Ошибка обновления пользователя: Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        // Проверка существования пользователя
        if (!userRepository.existsById(user.getId())) {
            log.error("Ошибка обновления пользователя: Пользователь с id = {} не найден", user.getId());
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", user.getId()));
        }

        // Валидация
        validateUser(user);

        // Если имя пустое, используем логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        User updatedUser = userRepository.update(user);
        log.info("Пользователь с id {} успешно обновлен", user.getId());
        return updatedUser;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Получение всех пользователей. Количество пользователей: {}", userRepository.findAll().size());
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(Integer id) {
        log.info("Удаление пользователя с id: {}", id);
        boolean result = userRepository.deleteById(id);
        if (result) {
            log.info("Пользователь с id {} успешно удален", id);
        } else {
            log.warn("Попытка удаления несуществующего пользователя с id: {}", id);
        }
        return result;
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        log.info("Получение пользователя по id: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        log.info("Добавление пользователя {} в друзья к пользователю {}", friendId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId)));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", friendId)));

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        userRepository.update(user);
        userRepository.update(friend);

        log.info("Пользователь {} добавлен в друзья к пользователю {}", friendId, userId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        log.info("Удаление пользователя {} из друзей пользователя {}", friendId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId)));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", friendId)));

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        userRepository.update(user);
        userRepository.update(friend);

        log.info("Пользователь {} удален из друзей пользователя {}", friendId, userId);
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        log.info("Получение списка друзей пользователя с id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId)));

        return user.getFriends().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        log.info("Получение общих друзей пользователей {} и {}", userId, otherUserId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId)));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", otherUserId)));

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Валидация пользователя
     * Проверяет корректность всех полей пользователя
     *
     * @param user пользователь для валидации
     * @throws ValidationException если пользователь не прошел валидацию
     */
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
}