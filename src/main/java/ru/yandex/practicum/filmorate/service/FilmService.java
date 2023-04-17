package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDao;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmDao filmDao;
    private final UserDao userDao;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;
    private final LikeDao likeDao;

    @Autowired
    public FilmService(FilmDao filmDao, UserDao userDao, GenreDao genreDao, MpaDao mpaDao, LikeDao likeDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
    }

    public void addLike(int filmId, int userId) {
        filmDao.showFilmById(filmId);
        userDao.showUserById(userId);
        likeDao.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmDao.showFilmById(filmId);
        userDao.showUserById(userId);
        likeDao.deleteLike(filmId, userId);
    }

    public List<Film> showPopularFilms(int count) {
        List<Integer> filmIds = likeDao.showLikesSort(count);
        Set<Film> films = new LinkedHashSet<>();
        filmIds.forEach(s -> films.add(filmDao.showFilmById(s)));
        films.addAll(showFilms());
        return films.stream().limit(count).collect(Collectors.toList());
    }


    public Film showFilmById(int id) {
        Film film = filmDao.showFilmById(id);
        collectorFilm(film);
        return film;
    }

    public List<Film> showFilms() {
        return filmDao.showFilms().stream()
                .peek(this::collectorFilm)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        Film filmGenre = filmDao.addFilm(film);
        genreDao.addGenres(filmGenre.getId(), film.getGenres());
        collectorFilm(filmGenre);
        return filmGenre;
    }


    public Film changeFilm(Film film) {
        Film filmGenre = filmDao.changeFilm(film);
        genreDao.updateGenres(filmGenre.getId(), film.getGenres());
        collectorFilm(filmGenre);
        return filmGenre;
    }

    public void deleteFilmById(int id) {
        filmDao.deleteFilmById(id);
    }

    private void collectorFilm(Film film) {
        film.setGenres(genreDao.getGenres(film.getId()));
    }
}
