package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.repository.MpaRatingRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Реализация сервиса для работы с рейтингами MPA.
 * Использует репозиторий для получения данных из хранилища.
 */
@Service
@RequiredArgsConstructor
public class MpaRatingServiceImpl implements MpaRatingService {

    private final MpaRatingRepository mpaRatingRepository;

    /**
     * Возвращает коллекцию всех рейтингов MPA.
     *
     * @return коллекция всех рейтингов MPA
     */
    @Override
    public Collection<MpaRating> getAllMpaRatings() {
        return mpaRatingRepository.findAll();
    }

    /**
     * Находит рейтинг MPA по его идентификатору.
     *
     * @param id идентификатор рейтинга MPA
     * @return Optional с рейтингом MPA, если найден, иначе пустой Optional
     */
    @Override
    public Optional<MpaRating> getMpaRatingById(Integer id) {
        return mpaRatingRepository.findById(id);
    }
}