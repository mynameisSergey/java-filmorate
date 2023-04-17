package ru.yandex.practicum.filmorate.exeption.validate;

public class FilmNameAlreadyExistException extends RuntimeException {
    public FilmNameAlreadyExistException(String message) {
        super(message);
    }
}