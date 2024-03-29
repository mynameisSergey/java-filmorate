package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film showFilmById(@PathVariable int id) {
        return filmService.showFilmById(id);
    }

    @GetMapping
    public List<Film> showFilms() {
        return filmService.showFilms();
    }


    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Request to add an element.");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("Request to change an element from id = {} .", film.getId());
        return filmService.changeFilm(film);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable int id) {
        log.info("Request to remove an element from id = {} .", id);
        filmService.deleteFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> showPopularFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        if (count <= 0) {
            throw new IncorrectParamException("Count param is incorrect.");
        }
        return filmService.showPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(name = "id") int filmId, @PathVariable int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable(name = "id") int filmId, @PathVariable int userId) {
        filmService.deleteLike(filmId, userId);
    }
}