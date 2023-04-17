package ru.yandex.practicum.filmorate.exeption.validate;

public class DateReleaseException extends RuntimeException {
    public DateReleaseException(String message) {
        super(message);
    }
}