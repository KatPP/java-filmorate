package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.InMemoryFilmRepository;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleFilmService implements FilmService {

    private final InMemoryFilmRepository filmRepository;
    private final InMemoryUserRepository userRepository;

    @Override
    public Film createFilm(Film film) throws ValidationException {
        log.info("Создание фильма: {}", film.getName());

        validateFilm(film);
        film.setId(filmRepository.getNextId());
        Film savedFilm = filmRepository.save(film);
        log.info("Фильм успешно создан с id: {}", savedFilm.getId());
        return savedFilm;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        log.info("Обновление фильма с id: {}", film.getId());

        // Проверка ID
        if (film.getId() == null) {
            log.error("Ошибка обновления фильма: Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        // Проверка существования фильма
        if (!filmRepository.existsById(film.getId())) {
            log.error("Ошибка обновления фильма: Фильм с id = {} не найден", film.getId());
            throw new NotFoundException(String.format("Фильм с id = %d не найден", film.getId()));
        }

        // Валидация
        validateFilm(film);

        Film updatedFilm = filmRepository.update(film);
        log.info("Фильм с id {} успешно обновлен", film.getId());
        return updatedFilm;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Получение всех фильмов. Количество фильмов: {}", filmRepository.findAll().size());
        return filmRepository.findAll();
    }

    @Override
    public boolean deleteFilm(Integer id) {
        log.info("Удаление фильма с id: {}", id);
        boolean result = filmRepository.deleteById(id);
        if (result) {
            log.info("Фильм с id {} успешно удален", id);
        } else {
            log.warn("Попытка удаления несуществующего фильма с id: {}", id);
        }
        return result;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        log.info("Получение фильма по id: {}", id);
        return filmRepository.findById(id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        log.info("Добавление лайка фильму {} от пользователя {}", filmId, userId);

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        film.getLikes().add(userId);
        filmRepository.update(film);

        log.info("Лайк добавлен фильму {} от пользователя {}", filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        log.info("Удаление лайка у фильма {} от пользователя {}", filmId, userId);

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));

        // Проверяем, существует ли пользователь
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        film.getLikes().remove(userId);
        filmRepository.update(film);

        log.info("Лайк удален у фильма {} от пользователя {}", filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (count == null || count <= 0) {
            count = 10;
        }

        log.info("Получение {} самых популярных фильмов", count);

        return filmRepository.findAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Валидация фильма
     * Проверяет корректность всех полей фильма
     *
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
}