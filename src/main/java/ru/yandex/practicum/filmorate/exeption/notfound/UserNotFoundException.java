package ru.yandex.practicum.filmorate.exeption.notfound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}