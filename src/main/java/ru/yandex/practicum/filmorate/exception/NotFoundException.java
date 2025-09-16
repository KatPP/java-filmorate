package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение, выбрасываемое когда запрашиваемая сущность не найдена.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public NotFoundException(String message) {
        super(message);
    }
}