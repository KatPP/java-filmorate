package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с пользователями.
 * Использует репозитории для работы с данными и реализует бизнес-логику.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    /**
     * Создает нового пользователя.
     *
     * @param user объект пользователя для создания
     * @return созданный пользователь с присвоенным ID
     * @throws ValidationException если пользователь не прошел валидацию
     */
    @Override
    public User createUser(User user) throws ValidationException {
        validateUser(user);
        return userRepository.save(user);
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param user объект пользователя с обновленными данными
     * @return обновленный пользователь
     * @throws ValidationException если пользователь не найден или не прошел валидацию
     */
    @Override
    public User updateUser(User user) throws ValidationException {
        validateUser(user);
        if (!userRepository.existsById(user.getId())) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", user.getId()));
        }
        return userRepository.update(user);
    }

    /**
     * Возвращает коллекцию всех пользователей.
     *
     * @return коллекция всех пользователей
     */
    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return Optional с пользователем, если найден, иначе пустой Optional
     */
    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя для удаления
     * @return true, если пользователь был удален, иначе false
     */
    @Override
    public boolean deleteUser(Integer id) {
        return userRepository.deleteById(id);
    }

    /**
     * Проверяет валидность данных пользователя.
     *
     * @param user объект пользователя для валидации
     * @throws ValidationException если данные пользователя не прошли валидацию
     */
    private void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email не может быть пустым и должен содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и не должен содержать пробелы");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday() == null || user.getBirthday().isAfter(java.time.LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    /**
     * Добавляет пользователя в друзья.
     *
     * @param userId идентификатор пользователя
     * @param friendId идентификатор друга
     * @throws NotFoundException если один из пользователей не найден
     */
    @Override
    public void addFriend(Integer userId, Integer friendId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }
        if (!userRepository.existsById(friendId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", friendId));
        }

        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(FriendshipStatus.PENDING);

        friendshipRepository.addFriendship(friendship);
    }

    /**
     * Удаляет пользователя из друзей.
     *
     * @param userId идентификатор пользователя
     * @param friendId идентификатор друга
     * @throws NotFoundException если один из пользователей не найден
     */
    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }
        if (!userRepository.existsById(friendId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", friendId));
        }

        friendshipRepository.removeFriendship(userId, friendId);
    }

    /**
     * Возвращает список друзей пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список друзей
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public Collection<User> getUserFriends(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }

        Collection<Integer> friendIds = friendshipRepository.getUserFriendsIds(userId);
        return friendIds.stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список общих друзей двух пользователей.
     *
     * @param userId идентификатор первого пользователя
     * @param otherId идентификатор второго пользователя
     * @return список общих друзей
     * @throws NotFoundException если один из пользователей не найден
     */
    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer otherId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }
        if (!userRepository.existsById(otherId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", otherId));
        }

        Collection<Integer> commonFriendIds = friendshipRepository.getCommonFriendsIds(userId, otherId);
        return commonFriendIds.stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }
}