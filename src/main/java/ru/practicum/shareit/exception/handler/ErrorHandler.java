package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemValidationException(final ItemValidationException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserValidationException(final UserValidationException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemUnvailableException(final ItemUnvailableException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFoundException(final ItemNotFoundException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemRequestNotFoundException(final ItemRequestNotFoundException e) {
        return new ErrorResponse(
            "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFoundException(final BookingNotFoundException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTimelineException(final TimelineException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDoubleApprovalException(final DoubleApprovalException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingErrorException(final BookingErrorException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCommentWithoutBookingException(final CommentWithoutBookingException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResp handleUnavailableException(final UnavailableException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResp(
                e.getMessage()
        );
    }
}
