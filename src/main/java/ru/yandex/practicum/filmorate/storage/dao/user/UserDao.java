package ru.yandex.practicum.filmorate.storage.dao.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserDao {

    List<User> showUsers();

    User showUserById(int id);

    User addUser(User user);

    User changeUser(User user);

    void deleteUserById(int id);
}