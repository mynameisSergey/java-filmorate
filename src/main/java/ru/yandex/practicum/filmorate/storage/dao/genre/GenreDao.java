package ru.yandex.practicum.filmorate.storage.dao.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDao {
    Genre showGenreById(int id);

    List<Genre> showGenres();

    void addGenres(int id, Set<Genre> genres);

    void updateGenres(int id, Set<Genre> genres);

    void deleteGenre(int id);

    Set<Genre> getGenres(int id);
}