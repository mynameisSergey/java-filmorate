package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.notfound.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.validate.FilmIdNotNullException;
import ru.yandex.practicum.filmorate.exeption.validate.DateReleaseException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmDao {
    private Map<Integer, Film> films;
   private Set<String> nameFilms;

    private static int filmID = 1;

    @Autowired
    public InMemoryFilmStorage() {
        films = new HashMap<>();
        nameFilms = new HashSet<>();
    }

    @Override
    public Film showFilmById(int id) {
        if (!(films.containsKey(id))) {
            log.warn("Не возможно найти film с id - {}.", id);
            throw new FilmNotFoundException(String.format("Cannot search film by %s.", id));
        }
        return films.get(id);
    }

    @Override
    public List<Film> showFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        checkFilm(film);
        nameFilms.add(film.getName());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }


    @Override
    public Film changeFilm(Film film) {
        checkFilm(film);
        films.remove(film.getId());
        films.put(film.getId(), film);
        nameFilms.add(film.getName());
        return films.get(film.getId());
    }

    @Override
    public void deleteFilmById(int id) {
        Film film = films.get(id);
        nameFilms.remove(film.getName());
        films.remove(id);

    }

    private void checkFilm(Film film) {

        Film filmCheck = null;
        // для проверки меняем или создаем нового
        if (films.containsKey(film.getId())) {
            filmCheck = films.get(film.getId());
        }
        if (nameFilms.contains(film.getName())) {
            if (filmCheck != null) {
                if (!film.getName().equals(filmCheck.getName())) {
                    log.warn("Film with name - \"{}\" already exist", film.getName());
                    throw new FilmIdNotNullException("Film with this name already exist. " +
                            "You cannot change Film's name.");
                }
            } else {
                log.warn("Film with name - \"{}\" already exist", film.getName());
                throw new FilmIdNotNullException("Film with this name already exist. " +
                        "You cannot add Film's name.");
            }
        }


        LocalDate localDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(localDate)) {
            log.warn("Ошибка добавление даты {}, дата должна быть после {}.", film.getReleaseDate(), localDate);
            throw new DateReleaseException("Date is wrong.");
        }

        //проверка id при создании
        if (filmCheck == null) {
            if (film.getId() == 0) {
                while (films.containsKey(filmID)) {
                    filmID++;
                }
                film.setId(filmID++);
            }
        }
    }
}