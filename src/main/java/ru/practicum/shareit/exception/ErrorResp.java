package ru.practicum.shareit.exception;


public class ErrorResp {
    private final String error;

    public ErrorResp(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}