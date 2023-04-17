package ru.yandex.practicum.filmorate.exeption.notfound;

public class LikeNotFoundException extends NotFoundException {
    public LikeNotFoundException(String message) {
        super(message);
    }
}