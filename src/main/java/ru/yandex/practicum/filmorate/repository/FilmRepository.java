package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {
    Film save(Film film);
    Film update(Film film);
    Collection<Film> findAll();
    Optional<Film> findById(Integer id);
    boolean existsById(Integer id);
    boolean deleteById(Integer id);
}