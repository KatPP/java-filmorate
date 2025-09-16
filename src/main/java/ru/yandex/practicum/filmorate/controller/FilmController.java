package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для работы с фильмами
 * REST API для создания, обновления, получения и удаления фильмов
 */
@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    /**
     * Создание нового фильма
     *
     * @param film объект фильма для создания
     * @return созданный фильм с присвоенным ID
     * @throws ValidationException если фильм не прошел валидацию
     */
    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на создание фильма: {}", film.getName());
        return filmService.createFilm(film);
    }

    /**
     * Обновление существующего фильма
     *
     * @param film объект фильма с обновленными данными
     * @return обновленный фильм
     * @throws ValidationException если фильм не найден или не прошел валидацию
     */
    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на обновление фильма с id: {}", film.getId());
        return filmService.updateFilm(film);
    }

    /**
     * Получение всех фильмов
     *
     * @return коллекция всех фильмов
     */
    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов");
        return filmService.getAllFilms();
    }

    /**
     * Получение фильма по ID
     *
     * @param id идентификатор фильма
     * @return фильм
     */
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("Получен запрос на получение фильма с id: {}", id);
        Optional<Film> film = filmService.getFilmById(id);
        if (film.isPresent()) {
            return film.get();
        } else {
            log.warn("Фильм с id {} не найден", id);
            throw new IllegalArgumentException(String.format("Фильм с id = %d не найден", id));
        }
    }

    /**
     * Удаление фильма по ID
     *
     * @param id идентификатор фильма для удаления
     * @return статус 204 No Content при успешном удалении
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable Integer id) {
        log.info("Получен запрос на удаление фильма с id: {}", id);
        boolean deleted = filmService.deleteFilm(id);
        if (!deleted) {
            log.warn("Попытка удаления несуществующего фильма с id: {}", id);
            throw new IllegalArgumentException(String.format("Фильм с id = %d не найден", id));
        }
    }

    /**
     * Добавление лайка фильму
     *
     * @param id     идентификатор фильма
     * @param userId идентификатор пользователя
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос на добавление лайка фильму {} от пользователя {}", id, userId);
        filmService.addLike(id, userId);
    }

    /**
     * Удаление лайка у фильма
     *
     * @param id     идентификатор фильма
     * @param userId идентификатор пользователя
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос на удаление лайка у фильма {} от пользователя {}", id, userId);
        filmService.removeLike(id, userId);
    }

    /**
     * Получение популярных фильмов
     *
     * @param count количество фильмов (по умолчанию 10)
     * @return список популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        log.info("Получен запрос на получение популярных фильмов, count: {}", count);
        return filmService.getPopularFilms(count);
    }
}