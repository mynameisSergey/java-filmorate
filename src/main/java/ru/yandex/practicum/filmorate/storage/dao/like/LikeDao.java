package ru.yandex.practicum.filmorate.storage.dao.like;

import java.util.List;

public interface LikeDao {

    void addLike(int filmID, int userID);

    void deleteLike(int filmID, int userID);

    List<Integer> showLikesSort(int count);
}
