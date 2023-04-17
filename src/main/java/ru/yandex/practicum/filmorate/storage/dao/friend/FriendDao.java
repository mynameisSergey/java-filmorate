package ru.yandex.practicum.filmorate.storage.dao.friend;

import java.util.List;

public interface FriendDao {
    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<Integer> showFriendsById(int id);

}