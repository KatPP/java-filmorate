package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmService {
    Film createFilm(Film film) throws ValidationException;
    Film updateFilm(Film film) throws ValidationException;
    Collection<Film> getAllFilms();
    boolean deleteFilm(Integer id);
    Optional<Film> getFilmById(Integer id);

    // Методы для работы с лайками
    void addLike(Integer filmId, Integer userId);
    void removeLike(Integer filmId, Integer userId);
    List<Film> getPopularFilms(Integer count);
}