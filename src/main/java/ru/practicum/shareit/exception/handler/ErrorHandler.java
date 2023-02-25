package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.exception.UserValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

        @ExceptionHandler
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ErrorResponse handleItemValidationException(final ItemValidationException e) {
            // возвращаем сообщение об ошибке
            return new ErrorResponse(
                    "Объект не найден", e.getMessage()
            );
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        // возвращаем сообщение об ошибке
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserValidationException(final UserValidationException e) {
        // возвращаем сообщение об ошибке
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }
}
