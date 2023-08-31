package ru.practicum.shareit.exception;

public class StatusDoesntExistException extends UnavailableException {
    public StatusDoesntExistException(String status) {
        super(String.format("Unknown state: %s", status));
    }
}

