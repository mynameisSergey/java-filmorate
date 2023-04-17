package ru.yandex.practicum.filmorate.exeption.notfound;

public class GenreNotFoundException extends NotFoundException {
    public GenreNotFoundException(String message) {
        super(message);
    }
}