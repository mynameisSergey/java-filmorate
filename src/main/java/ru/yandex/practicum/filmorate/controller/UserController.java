package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.notfound.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/users")
@Slf4j
@ResponseStatus(HttpStatus.OK)
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User showUserById(@PathVariable int id) {
        return userService.showUserById(id);
    }


    @GetMapping
    public List<User> showUsers() {
        log.info("Запрос на вывод всех элеметов");
        return userService.showUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Запросто на добавление элемента.");
        return userService.addUser(user);
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        log.info("Запросто на изменения элемента.");
        return userService.changeUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> showFriends(@PathVariable int id) {
        return userService.showFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        if (id == friendId) {
            throw new UserNotFoundException("The user cannot be his own friend");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> showCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.showCommonFriends(id, otherId);
    }

}