package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Реализация хранилища фильмов с использованием JDBC.
 * Отвечает за сохранение, обновление, получение и удаление фильмов из базы данных.
 */
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Маппер для преобразования результата запроса в объект Film.
     */
    private final RowMapper<Film> filmRowMapper = (rs, rowNum) -> {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        // Устанавливаем MPA рейтинг
        if (rs.getObject("mpa_rating_id") != null) {
            MpaRating mpa = new MpaRating();
            mpa.setId(rs.getInt("mpa_rating_id"));
            // Имя рейтинга будет установлено отдельно при необходимости
            film.setMpa(mpa);
        }

        return film;
    };

    /**
     * Сохраняет новый фильм в базе данных.
     *
     * @param film объект фильма для сохранения
     * @return сохраненный фильм с присвоенным ID
     */
    @Override
    public Film save(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setObject(5, film.getMpa() != null ? film.getMpa().getId() : null);
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

        // Сохраняем жанры фильма
        saveFilmGenres(film);

        return film;
    }

    /**
     * Обновляет существующий фильм в базе данных.
     *
     * @param film объект фильма с обновленными данными
     * @return обновленный фильм
     * @throws NotFoundException если фильм с указанным ID не найден
     */
    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? WHERE film_id = ?";
        int updated = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());

        if (updated == 0) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        // Обновляем жанры фильма
        updateFilmGenres(film);

        return film;
    }

    /**
     * Возвращает коллекцию всех фильмов из базы данных.
     *
     * @return коллекция всех фильмов
     */
    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT f.*, m.name as mpa_name FROM films f " +
                "LEFT JOIN mpa_ratings m ON f.mpa_rating_id = m.mpa_rating_id";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    /**
     * Находит фильм по его идентификатору.
     *
     * @param id идентификатор фильма
     * @return Optional с фильмом, если найден, иначе пустой Optional
     */
    @Override
    public Optional<Film> findById(Integer id) {
        String sql = "SELECT f.*, m.name as mpa_name FROM films f " +
                "LEFT JOIN mpa_ratings m ON f.mpa_rating_id = m.mpa_rating_id " +
                "WHERE f.film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, filmRowMapper, id);
            if (film != null) {
                loadFilmGenres(film);
                loadFilmMpa(film);
            }
            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Проверяет существование фильма с указанным идентификатором.
     *
     * @param id идентификатор фильма
     * @return true, если фильм существует, иначе false
     */
    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM films WHERE film_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    /**
     * Удаляет фильм по его идентификатору.
     *
     * @param id идентификатор фильма для удаления
     * @return true, если фильм был удален, иначе false
     */
    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }

    /**
     * Сохраняет жанры фильма в базе данных.
     *
     * @param film фильм, для которого сохраняются жанры
     */
    private void saveFilmGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
    }

    /**
     * Обновляет жанры фильма в базе данных (удаляет старые и добавляет новые).
     *
     * @param film фильм, для которого обновляются жанры
     */
    private void updateFilmGenres(Film film) {
        // Удаляем старые жанры
        String deleteSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, film.getId());

        // Добавляем новые жанры
        saveFilmGenres(film);
    }

    /**
     * Загружает жанры фильма из базы данных.
     *
     * @param film фильм, для которого загружаются жанры
     */
    private void loadFilmGenres(Film film) {
        String sql = "SELECT g.genre_id, g.name FROM genres g " +
                "JOIN film_genres fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, film.getId()));
        film.setGenres(genres);
    }

    /**
     * Загружает имя MPA рейтинга из базы данных.
     *
     * @param film фильм, для которого загружается имя MPA рейтинга
     */
    private void loadFilmMpa(Film film) {
        if (film.getMpa() != null && film.getMpa().getId() != null) {
            String sql = "SELECT name FROM mpa_ratings WHERE mpa_rating_id = ?";
            try {
                String mpaName = jdbcTemplate.queryForObject(sql, String.class, film.getMpa().getId());
                if (mpaName != null) {
                    film.getMpa().setName(mpaName);
                }
            } catch (EmptyResultDataAccessException e) {
                // Если рейтинг не найден, оставляем как есть
            }
        }
    }
}