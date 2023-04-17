package ru.yandex.practicum.filmorate.exeption.validate;

public class IncorrectParamException extends RuntimeException {
    public IncorrectParamException(String message) {
        super(message);
    }
}