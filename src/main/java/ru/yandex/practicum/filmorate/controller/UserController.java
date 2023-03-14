package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")

public class UserController {

    private final Set<User> users = new HashSet<>();
    private int idGenerator = 1;

    @GetMapping()
    public Set<User> findAllUsers() {
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        if (validation(user)) {
            user.setId(idGenerator++);
            users.add(user);
            log.info(user.toString());
            return user;
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) throws ValidationException {
        if (validation(newUser)) {
            for (User user : users) {
                if (user.getId() == newUser.getId()) {
                    users.remove(user);
                    users.add(newUser);
                    log.info(newUser.toString());
                    return newUser;
                } else {
                    log.warn("Обновление не произошло");
                    throw new ValidationException("Пользователя с id:" + newUser.getId() + "не существует");
                }
            }
        }
        return newUser;
    }

    private boolean validation(User user) throws ValidationException {
        if (!StringUtils.hasText(user.getEmail())) {
            log.warn("Неправильно ввели адрес электронной почты");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        for (User user1 : users) {
            if (user1.getEmail().equals(user.getEmail())) {
                log.warn("Неправильно ввели адрес электронной почты");
                throw new ValidationException("Пользователь с электронной почтой " + user.getEmail() + " уже зарегистрирован.");
            }
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Неправильно ввели адрес электронной почты");
            throw new ValidationException("Адрес электронной почты не содержит @.");
        }
        if (!StringUtils.hasText(user.getLogin())) {
            log.warn("Логин пустой");
            throw new ValidationException("Логин не может быть пустым.");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Неправильно ввели логин");
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (!StringUtils.hasText(user.getName())) {
            log.warn("Не ввели имя,используется логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Не корректно ввели дату рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return true;
    }
}