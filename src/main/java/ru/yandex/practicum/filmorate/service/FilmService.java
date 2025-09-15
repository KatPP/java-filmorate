package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс сервиса для работы с фильмами.
 * Определяет методы для создания, обновления, получения и удаления фильмов,
 * а также для работы с лайками и получения популярных фильмов.
 */
public interface FilmService {

    /**
     * Создает новый фильм.
     *
     * @param film объект фильма для создания
     * @return созданный фильм с присвоенным ID
     * @throws ValidationException если фильм не прошел валидацию
     */
    Film createFilm(Film film) throws ValidationException;

    /**
     * Обновляет существующий фильм.
     *
     * @param film объект фильма с обновленными данными
     * @return обновленный фильм
     * @throws ValidationException если фильм не найден или не прошел валидацию
     */
    Film updateFilm(Film film) throws ValidationException;

    /**
     * Возвращает коллекцию всех фильмов.
     *
     * @return коллекция всех фильмов
     */
    Collection<Film> getAllFilms();

    /**
     * Находит фильм по его идентификатору.
     *
     * @param id идентификатор фильма
     * @return Optional с фильмом, если найден, иначе пустой Optional
     */
    Optional<Film> getFilmById(Integer id);

    /**
     * Удаляет фильм по его идентификатору.
     *
     * @param id идентификатор фильма для удаления
     * @return true, если фильм был удален, иначе false
     */
    boolean deleteFilm(Integer id);

    /**
     * Добавляет лайк фильму от пользователя.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    void addLike(Integer filmId, Integer userId);

    /**
     * Удаляет лайк у фильма от пользователя.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    void removeLike(Integer filmId, Integer userId);

    /**
     * Возвращает список популярных фильмов.
     *
     * @param count количество фильмов (по умолчанию 10)
     * @return список популярных фильмов
     */
    List<Film> getPopularFilms(Integer count);
}