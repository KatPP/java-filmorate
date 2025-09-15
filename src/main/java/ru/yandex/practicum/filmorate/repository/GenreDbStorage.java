package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

/**
 * Реализация хранилища жанров с использованием JDBC.
 * Отвечает за получение жанров из базы данных.
 */
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Маппер для преобразования результата запроса в объект Genre.
     */
    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("name"));
        return genre;
    };

    /**
     * Возвращает коллекцию всех жанров из базы данных.
     *
     * @return коллекция всех жанров
     */
    @Override
    public Collection<Genre> findAll() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, genreRowMapper);
    }

    /**
     * Находит жанр по его идентификатору.
     *
     * @param id идентификатор жанра
     * @return Optional с жанром, если найден, иначе пустой Optional
     */
    @Override
    public Optional<Genre> findById(Integer id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, genreRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}