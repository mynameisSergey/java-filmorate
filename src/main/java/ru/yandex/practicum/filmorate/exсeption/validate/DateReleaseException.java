package ru.yandex.practicum.filmorate.exсeption.validate;

public class DateReleaseException extends RuntimeException {
    public DateReleaseException(String message) {
        super(message);
    }
}