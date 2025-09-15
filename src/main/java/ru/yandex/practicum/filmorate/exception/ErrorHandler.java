package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Обрабатывает исключения валидации.
     *
     * @param e исключение валидации
     * @return объект с описанием ошибки
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("Ошибка валидации: {}", e.getMessage());
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    /**
     * Обрабатывает исключения "не найдено".
     *
     * @param e исключение "не найдено"
     * @return объект с описанием ошибки
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("Ошибка: {}", e.getMessage());
        return new ErrorResponse("Ошибка запроса", e.getMessage());
    }

    /**
     * Обрабатывает все остальные исключения.
     *
     * @param e исключение
     * @return объект с описанием ошибки
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Непредвиденная ошибка: ", e);
        return new ErrorResponse("Произошла непредвиденная ошибка", e.getMessage());
    }
}