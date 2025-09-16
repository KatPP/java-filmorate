package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

/**
 * Реализация хранилища рейтингов MPA с использованием JDBC.
 * Отвечает за получение рейтингов MPA из базы данных.
 */
@Repository
@RequiredArgsConstructor
public class MpaRatingDbStorage implements MpaRatingRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Маппер для преобразования результата запроса в объект MpaRating.
     */
    private final RowMapper<MpaRating> mpaRatingRowMapper = (rs, rowNum) -> {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(rs.getInt("mpa_rating_id"));
        mpaRating.setName(rs.getString("name"));
        return mpaRating;
    };

    /**
     * Возвращает коллекцию всех рейтингов MPA из базы данных.
     *
     * @return коллекция всех рейтингов MPA
     */
    @Override
    public Collection<MpaRating> findAll() {
        String sql = "SELECT * FROM mpa_ratings";
        return jdbcTemplate.query(sql, mpaRatingRowMapper);
    }

    /**
     * Находит рейтинг MPA по его идентификатору.
     *
     * @param id идентификатор рейтинга MPA
     * @return Optional с рейтингом MPA, если найден, иначе пустой Optional
     */
    @Override
    public Optional<MpaRating> findById(Integer id) {
        String sql = "SELECT * FROM mpa_ratings WHERE mpa_rating_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, mpaRatingRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}