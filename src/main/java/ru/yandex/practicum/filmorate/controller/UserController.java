package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для работы с пользователями
 * REST API для создания, обновления, получения и удаления пользователей
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Создание нового пользователя
     *
     * @param user объект пользователя для создания
     * @return созданный пользователь с присвоенным ID
     * @throws ValidationException если пользователь не прошел валидацию
     */
    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос на создание пользователя: {}", user.getLogin());
        return userService.createUser(user);
    }

    /**
     * Обновление существующего пользователя
     *
     * @param user объект пользователя с обновленными данными
     * @return обновленный пользователь
     * @throws ValidationException если пользователь не найден или не прошел валидацию
     */
    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос на обновление пользователя с id: {}", user.getId());
        return userService.updateUser(user);
    }

    /**
     * Получение всех пользователей
     *
     * @return коллекция всех пользователей
     */
    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userService.getAllUsers();
    }

    /**
     * Получение пользователя по ID
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("Получен запрос на получение пользователя с id: {}", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            log.warn("Пользователь с id {} не найден", id);
            throw new IllegalArgumentException("Пользователь с id = " + id + " не найден");
        }
    }

    /**
     * Удаление пользователя по ID
     *
     * @param id идентификатор пользователя для удаления
     * @return статус 204 No Content при успешном удалении
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        log.info("Получен запрос на удаление пользователя с id: {}", id);
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            log.warn("Попытка удаления несуществующего пользователя с id: {}", id);
            throw new IllegalArgumentException("Пользователь с id = " + id + " не найден");
        }
    }

    /**
     * Добавление в друзья
     *
     * @param id       идентификатор пользователя
     * @param friendId идентификатор друга
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос на добавление пользователя {} в друзья к пользователю {}", friendId, id);
        userService.addFriend(id, friendId);
    }

    /**
     * Удаление из друзей
     *
     * @param id       идентификатор пользователя
     * @param friendId идентификатор друга
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос на удаление пользователя {} из друзей пользователя {}", friendId, id);
        userService.removeFriend(id, friendId);
    }

    /**
     * Получение списка друзей пользователя
     *
     * @param id идентификатор пользователя
     * @return список друзей
     */
    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        log.info("Получен запрос на получение списка друзей пользователя с id: {}", id);
        return userService.getUserFriends(id);
    }

    /**
     * Получение списка общих друзей
     *
     * @param id      идентификатор пользователя
     * @param otherId идентификатор другого пользователя
     * @return список общих друзей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Получен запрос на получение общих друзей пользователей {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}