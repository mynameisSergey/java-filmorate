package ru.yandex.practicum.filmorate.exeption.validate;

public class FilmIdNotNullException extends RuntimeException {
    public FilmIdNotNullException(String message) {
        super(message);
    }
}