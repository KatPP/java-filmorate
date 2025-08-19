package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

/**
 * Контроллер для работы с фильмами
 * REST API для создания, обновления и получения фильмов
 */
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    /**
     * Создание нового фильма
     * @param film объект фильма для создания
     * @return созданный фильм с присвоенным ID
     * @throws ValidationException если фильм не прошел валидацию
     */
    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на создание фильма: {}", film.getName());

        try {
            validateFilm(film);
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Фильм успешно создан с id: {}", film.getId());
            return film;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при создании фильма: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Обновление существующего фильма
     * @param film объект фильма с обновленными данными
     * @return обновленный фильм
     * @throws ValidationException если фильм не найден или не прошел валидацию
     */
    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на обновление фильма с id: {}", film.getId());

        // Проверка ID
        if (film.getId() == null) {
            log.error("Ошибка обновления фильма: Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        // Проверка существования фильма
        if (!films.containsKey(film.getId())) {
            log.error("Ошибка обновления фильма: Фильм с id = {} не найден", film.getId());
            throw new ValidationException(String.format("Фильм с id = %d не найден", film.getId()));
        }

        // Валидация
        validateFilm(film);

        // Обновляем фильм в хранилище
        films.put(film.getId(), film);
        log.info("Фильм с id {} успешно обновлен", film.getId());
        return film;
    }

    /**
     * Получение всех фильмов
     * @return коллекция всех фильмов
     */
    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов. Количество фильмов: {}", films.size());
        return films.values();
    }

    /**
     * Валидация фильма
     * Проверяет корректность всех полей фильма
     * @param film фильм для валидации
     * @throws ValidationException если фильм не прошел валидацию
     */
    private void validateFilm(Film film) throws ValidationException {
        // Проверка названия
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Валидация не пройдена: Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        // Проверка описания
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Валидация не пройдена: Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }

        // Проверка даты релиза
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Валидация не пройдена: Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        // Проверка продолжительности
        if (film.getDuration() != null && film.getDuration() <= 0) {
            log.warn("Валидация не пройдена: Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    /**
     * Метод для генерации следующего уникального ID
     * @return следующий доступный ID
     */
    private int getNextId() {
        return films.keySet()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }
}