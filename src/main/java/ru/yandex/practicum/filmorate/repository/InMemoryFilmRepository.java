package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryFilmRepository implements FilmRepository {

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film save(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Optional<Film> findById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public boolean existsById(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public boolean deleteById(Integer id) {
        return films.remove(id) != null;
    }

    public int getNextId() {
        return films.keySet()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }
}