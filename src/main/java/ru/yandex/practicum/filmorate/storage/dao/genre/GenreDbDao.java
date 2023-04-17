package ru.yandex.practicum.filmorate.storage.dao.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.notfound.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbDao implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre showGenreById(int genreID) {
        log.info("запрос на вывод жанра по  GENRE_ID");
        try {
            Genre genre = jdbcTemplate.queryForObject(format(""
                    + "SELECT genre_id, name "
                    + "FROM genres "
                    + "WHERE genre_id=%d", genreID), new GenreMapper());
            return genre;
        } catch (
                EmptyResultDataAccessException e) {
            log.error("Не возможно найти genre с  - {}.", genreID);
            throw new GenreNotFoundException(format("Не возможно найти genre с  - %s.", genreID));
        }
    }

    @Override
    public List<Genre> showGenres() {
        log.info("запрос на вывод списка всех жанров");

        List<Genre> result = jdbcTemplate.query(""
                + "SELECT genre_id, name "
                + "FROM genres "
                + "ORDER BY genre_id", new GenreMapper());
        return result;

    }

    @Override
    public void deleteGenre(int filmID) {
        log.info("запрос на удаление списка жанров по  FILM_ID");
        jdbcTemplate.update(""
                + "DELETE "
                + "FROM film_genres "
                + "WHERE film_id=?", filmID);
    }

    @Override
    public void addGenres(int filmID, Set<Genre> genres) {
        log.info("запрос на добавление списка жанров по  FILM_ID");
        for (Genre genre : genres) {
            jdbcTemplate.update(""
                    + "INSERT INTO film_genres (film_id, genre_id) "
                    + "VALUES (?, ?)", filmID, genre.getId());
        }
    }

    @Override
    public void updateGenres(int filmID, Set<Genre> genres) {
        log.info("запрос на обновление списка жанров по  FILM_ID");
        deleteGenre(filmID);
        addGenres(filmID, genres);
    }

    @Override
    public Set<Genre> getGenres(int filmID) {
        log.info("запрос список жанров по   FILM_ID");

        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(format(""
                + "SELECT f.genre_id, g.name "
                + "FROM film_genres AS f "
                + "LEFT OUTER JOIN genres AS g ON f.genre_id = g.genre_id "
                + "WHERE f.film_id=%d "
                + "ORDER BY g.genre_id", filmID), new GenreMapper()));
        return genres;
    }
}