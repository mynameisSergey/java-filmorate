package ru.yandex.practicum.filmorate.exeption.validate;

public class UserIdNotNullException extends RuntimeException {
    public UserIdNotNullException(String message) {
        super(message);
    }
}