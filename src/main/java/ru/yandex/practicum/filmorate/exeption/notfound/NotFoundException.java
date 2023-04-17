package ru.yandex.practicum.filmorate.exeption.notfound;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
