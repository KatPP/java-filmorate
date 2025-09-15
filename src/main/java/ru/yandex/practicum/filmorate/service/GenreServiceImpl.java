package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Реализация сервиса для работы с жанрами фильмов.
 * Использует репозиторий для получения данных из хранилища.
 */
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    /**
     * Возвращает коллекцию всех жанров.
     *
     * @return коллекция всех жанров
     */
    @Override
    public Collection<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    /**
     * Находит жанр по его идентификатору.
     *
     * @param id идентификатор жанра
     * @return Optional с жанром, если найден, иначе пустой Optional
     */
    @Override
    public Optional<Genre> getGenreById(Integer id) {
        return genreRepository.findById(id);
    }
}