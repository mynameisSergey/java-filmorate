package ru.yandex.practicum.filmorate.exeption.validate;

public class UserLoginAlreadyExistException extends RuntimeException {
    public UserLoginAlreadyExistException(String message) {
        super(message);
    }
}