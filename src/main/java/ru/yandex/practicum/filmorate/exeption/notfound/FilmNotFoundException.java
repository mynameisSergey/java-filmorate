package ru.yandex.practicum.filmorate.exeption.notfound;

public class FilmNotFoundException extends NotFoundException {
    public FilmNotFoundException(String message) {
        super(message);
    }
}