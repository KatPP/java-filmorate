package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционные тесты для FilmDbStorage.
 * Проверяют работу с базой данных для фильмов.
 */
@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class})
class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage filmStorage;

    /**
     * Тест сохранения фильма.
     */
    @Test
    void testSaveFilm() {
        Film film = new Film();
        film.setName("Тестовый фильм");
        film.setDescription("Описание тестового фильма");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        MpaRating mpa = new MpaRating();
        mpa.setId(1);
        film.setMpa(mpa);

        Film savedFilm = filmStorage.save(film);

        assertThat(savedFilm.getId()).isNotNull();
        assertThat(savedFilm.getName()).isEqualTo("Тестовый фильм");
        assertThat(savedFilm.getDescription()).isEqualTo("Описание тестового фильма");
        assertThat(savedFilm.getReleaseDate()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(savedFilm.getDuration()).isEqualTo(120);
        assertThat(savedFilm.getMpa().getId()).isEqualTo(1);
    }

    /**
     * Тест поиска фильма по ID.
     */
    @Test
    void testFindFilmById() {
        Film film = new Film();
        film.setName("Тестовый фильм 2");
        film.setDescription("Описание тестового фильма 2");
        film.setReleaseDate(LocalDate.of(2005, 5, 5));
        film.setDuration(150);

        MpaRating mpa = new MpaRating();
        mpa.setId(2);
        film.setMpa(mpa);

        Film savedFilm = filmStorage.save(film);
        Optional<Film> foundFilm = filmStorage.findById(savedFilm.getId());

        assertThat(foundFilm).isPresent();
        assertThat(foundFilm.get().getId()).isEqualTo(savedFilm.getId());
        assertThat(foundFilm.get().getName()).isEqualTo("Тестовый фильм 2");
    }

    /**
     * Тест обновления фильма.
     */
    @Test
    void testUpdateFilm() {
        Film film = new Film();
        film.setName("Оригинальный фильм");
        film.setDescription("Описание оригинального фильма");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        MpaRating mpa = new MpaRating();
        mpa.setId(1);
        film.setMpa(mpa);

        Film savedFilm = filmStorage.save(film);

        savedFilm.setName("Обновленный фильм");
        savedFilm.setDuration(110);

        Film updatedFilm = filmStorage.update(savedFilm);

        assertThat(updatedFilm.getName()).isEqualTo("Обновленный фильм");
        assertThat(updatedFilm.getDuration()).isEqualTo(110);
    }

    /**
     * Тест удаления фильма.
     */
    @Test
    void testDeleteFilm() {
        Film film = new Film();
        film.setName("Фильм для удаления");
        film.setDescription("Описание фильма для удаления");
        film.setReleaseDate(LocalDate.of(2010, 10, 10));
        film.setDuration(90);

        MpaRating mpa = new MpaRating();
        mpa.setId(3);
        film.setMpa(mpa);

        Film savedFilm = filmStorage.save(film);
        boolean deleted = filmStorage.deleteById(savedFilm.getId());

        assertThat(deleted).isTrue();
        assertThat(filmStorage.findById(savedFilm.getId())).isEmpty();
    }
}