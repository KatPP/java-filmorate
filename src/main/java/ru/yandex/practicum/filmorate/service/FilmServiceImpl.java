package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с фильмами.
 * Использует репозитории для работы с данными и реализует бизнес-логику.
 */
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final MpaRatingRepository mpaRatingRepository;
    private final GenreRepository genreRepository;

    /**
     * Создает новый фильм.
     *
     * @param film объект фильма для создания
     * @return созданный фильм с присвоенным ID
     * @throws ValidationException если фильм не прошел валидацию
     * @throws NotFoundException если MPA или жанр не найдены
     */
    @Override
    public Film createFilm(Film film) throws ValidationException, NotFoundException {
        validateFilm(film);
        return filmRepository.save(film);
    }

    /**
     * Обновляет существующий фильм.
     *
     * @param film объект фильма с обновленными данными
     * @return обновленный фильм
     * @throws ValidationException если фильм не найден или не прошел валидацию
     * @throws NotFoundException если фильм или MPA/жанр не найдены
     */
    @Override
    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        validateFilm(film);
        if (!filmRepository.existsById(film.getId())) {
            throw new NotFoundException(String.format("Фильм с id = %d не найден", film.getId()));
        }
        return filmRepository.update(film);
    }

    /**
     * Возвращает коллекцию всех фильмов.
     *
     * @return коллекция всех фильмов
     */
    @Override
    public Collection<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    /**
     * Находит фильм по его идентификатору.
     *
     * @param id идентификатор фильма
     * @return Optional с фильмом, если найден, иначе пустой Optional
     */
    @Override
    public Optional<Film> getFilmById(Integer id) {
        return filmRepository.findById(id);
    }

    /**
     * Удаляет фильм по его идентификатору.
     *
     * @param id идентификатор фильма для удаления
     * @return true, если фильм был удален, иначе false
     */
    @Override
    public boolean deleteFilm(Integer id) {
        return filmRepository.deleteById(id);
    }

    /**
     * Проверяет валидность данных фильма.
     *
     * @param film объект фильма для валидации
     * @throws ValidationException если данные фильма не прошли валидацию
     * @throws NotFoundException если MPA или жанр не найдены
     */
    private void validateFilm(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() != null && film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        // Проверка MPA
        if (film.getMpa() != null && film.getMpa().getId() != null) {
            if (!mpaRatingRepository.findById(film.getMpa().getId()).isPresent()) {
                throw new NotFoundException(String.format("Рейтинг MPA с id = %d не найден", film.getMpa().getId()));
            }
        }

        // Проверка жанров
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() != null && !genreRepository.findById(genre.getId()).isPresent()) {
                    throw new NotFoundException(String.format("Жанр с id = %d не найден", genre.getId()));
                }
            }
        }
    }

    /**
     * Добавляет лайк фильму от пользователя.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     * @throws NotFoundException если фильм или пользователь не найден
     */
    @Override
    public void addLike(Integer filmId, Integer userId) {
        if (!filmRepository.existsById(filmId)) {
            throw new NotFoundException(String.format("Фильм с id = %d не найден", filmId));
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }

        likeRepository.addLike(filmId, userId);
    }

    /**
     * Удаляет лайк у фильма от пользователя.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     * @throws NotFoundException если фильм или пользователь не найден
     */
    @Override
    public void removeLike(Integer filmId, Integer userId) {
        if (!filmRepository.existsById(filmId)) {
            throw new NotFoundException(String.format("Фильм с id = %d не найден", filmId));
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }

        likeRepository.removeLike(filmId, userId);
    }

    /**
     * Возвращает список популярных фильмов.
     *
     * @param count количество фильмов (по умолчанию 10)
     * @return список популярных фильмов
     */
    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (count == null || count <= 0) {
            count = 10;
        }

        Collection<Film> allFilms = filmRepository.findAll();
        return allFilms.stream()
                .sorted((f1, f2) -> {
                    int likes1 = likeRepository.getLikesCount(f1.getId());
                    int likes2 = likeRepository.getLikesCount(f2.getId());
                    return Integer.compare(likes2, likes1);
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}