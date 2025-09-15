package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.Collection;

/**
 * Контроллер для работы с рейтингами MPA.
 * REST API для получения списка всех рейтингов и получения рейтинга по идентификатору.
 */
@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaRatingController {

    private final MpaRatingService mpaRatingService;

    /**
     * Возвращает список всех рейтингов MPA.
     *
     * @return коллекция всех рейтингов MPA
     */
    @GetMapping
    public Collection<MpaRating> getAllMpaRatings() {
        log.info("Получен запрос на получение всех рейтингов MPA");
        return mpaRatingService.getAllMpaRatings();
    }

    /**
     * Возвращает рейтинг MPA по его идентификатору.
     *
     * @param id идентификатор рейтинга MPA
     * @return рейтинг MPA
     */
    @GetMapping("/{id}")
    public MpaRating getMpaRatingById(@PathVariable Integer id) {
        log.info("Получен запрос на получение рейтинга MPA с id: {}", id);
        return mpaRatingService.getMpaRatingById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id = " + id + " не найден"));
    }
}