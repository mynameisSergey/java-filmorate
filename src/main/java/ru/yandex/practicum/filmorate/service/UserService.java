package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.friend.FriendDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDao;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;
    private final FriendDao friendDao;

    @Autowired
    public UserService(UserDao userDao, FriendDao friendDao) {
        this.userDao = userDao;
        this.friendDao = friendDao;
    }

    public void addFriend(int userID, int friendID) {
        checkUser(userID);
        checkUser(friendID);
        friendDao.addFriend(userID, friendID);
    }

    public void deleteFriend(int userID, int friendID) {
        checkUser(userID);
        checkUser(friendID);
        friendDao.deleteFriend(userID, friendID);
    }

    public List<User> showCommonFriends(int userID, int friendID) {
        checkUser(userID);
        checkUser(friendID);
        List<User> user = showFriends(userID);
        List<User> friend = showFriends(friendID);
        user.retainAll(friend);
        return user;
    }

    public List<User> showFriends(int userID) {
        checkUser(userID);
        List<Integer> friendId = friendDao.showFriendsById(userID);
        List<User> users = new ArrayList<>();
        for (int id : friendId) {
            checkUser(id);
            users.add(userDao.showUserById(id));
        }
        return users;
    }

    public List<User> showUsers() {
        return userDao.showUsers();
    }

    public User showUserById(int id) {
        return userDao.showUserById(id);
    }

    public User addUser(User user) {
        return userDao.addUser(user);
    }

    public User changeUser(User user) {
        return userDao.changeUser(user);
    }

    public void deleteUserById(int id) {
        userDao.deleteUserById(id);
    }

    private void checkUser(int userId) {
        showUserById(userId);
    }
}
