package ru.yandex.practicum.filmorate.exeption.validate;

public class FriendAlreadyExistException extends RuntimeException {
    public FriendAlreadyExistException(String message) {
        super(message);
    }
}