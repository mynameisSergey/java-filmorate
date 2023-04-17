package ru.yandex.practicum.filmorate.storage.dao.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.notfound.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeption.validate.UserLoginAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exeption.validate.FilmNameAlreadyExistException;

import java.sql.Date;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class UserDbDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<User> showUsers() {
        log.info("запрос на вывод всех пользователей");
        List<User> users = jdbcTemplate.query(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users", new UserMapper());
        return users;
    }

    @Override
    public User showUserById(int id) {
        log.info("запрос на вывод пользователя по id - {}", id);
        try {
            User user = jdbcTemplate.queryForObject(format(""
                    + "SELECT user_id, email, login, name, birthday "
                    + "FROM users "
                    + "WHERE user_id=%d", id), new UserMapper());

            return user;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Не возможно найти user с id - {}.", id);
            throw new UserNotFoundException(String.format("Не возможно найти user с id - %s.", id));
        }
    }

    @Override
    public User addUser(User user) {
        log.info("запрос на добавление пользователя - {}", user);
        checkAdd(user);

        jdbcTemplate.update(""
                        + "INSERT INTO users (email, login, name, birthday) "
                        + "VALUES (?, ?, ?, ?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));
        User result = jdbcTemplate.queryForObject(format(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users "
                + "WHERE email='%s'", user.getEmail()), new UserMapper());

        return result;
    }

    @Override
    public User changeUser(User user) {
        log.info("запрос на изменение пользователя - {}", user);
        checkChange(user);
        jdbcTemplate.update(""
                        + "UPDATE users "
                        + "SET email=?, login=?, name=?, birthday=? "
                        + "WHERE user_id=?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        User result = showUserById(user.getId());
        return result;
    }


    @Override
    public void deleteUserById(int id) {
        log.info("запрос на вывод удаление по id - {}", id);

        jdbcTemplate.update("DELETE " +
                "FROM users " +
                "WHERE user_id = ?", id);
    }

    private void checkAdd(User user) {
        log.info("проверка на добавление user - {}", user);

        if (user.getId() != 0) {
            log.error("user_id не должно иметь значение");
            throw new UserLoginAlreadyExistException("user_id не должно иметь значение");
        }


        try {
            User loginUser = jdbcTemplate.queryForObject(format(""
                    + "SELECT user_id, email, login, name, birthday "
                    + "FROM users "
                    + "WHERE login= '%s'", user.getLogin()), new UserMapper());

            log.error("Невозможно добить User. User c login - {} уже имеет ID - {}",
                    loginUser.getLogin(),
                    loginUser.getId());
            throw new UserLoginAlreadyExistException(
                    String.format("Невозможно добавить User. User c login - %s уже имеет ID - %s",
                            loginUser.getLogin(),
                            loginUser.getId()));

        } catch (EmptyResultDataAccessException e) { // эта ошибка обрабатывает NULL на выходе из базы,
            // иначе не нашел как мне получить или null или значение.
            log.info("User с login {} отсутствует!", user.getLogin());
        }

        try {
            User emailUser = jdbcTemplate.queryForObject(format(""
                    + "SELECT user_id, email, login, name, birthday "
                    + "FROM users "
                    + "WHERE name= '%s'", user.getEmail()), new UserMapper());

            log.error("Невозможно добавить user. User c email - {} уже имеет ID - {}",
                    emailUser.getEmail(),
                    emailUser.getId());
            throw new UserLoginAlreadyExistException(
                    String.format("Невозможно добавить user. User c email - %s уже имеет ID - %s",
                            emailUser.getEmail(),
                            emailUser.getId()));

        } catch (EmptyResultDataAccessException e) {
            log.info("User с email {} отсутствует!", user.getEmail());
        }

        if (user.getName().isBlank()) {
            log.debug("User не имеет имени");
            user.setName(user.getLogin());
        }
    }

    private void checkChange(User user) {
        log.info("проверка на изменение user - {}", user);
        showUserById(user.getId());
        try {
            User loginUser = jdbcTemplate.queryForObject(format(""
                    + "SELECT user_id, email, login, name, birthday "
                    + "FROM users "
                    + "WHERE login= '%s'", user.getLogin()), new UserMapper());

            if (loginUser.getId() != user.getId()) {
                log.error("Невозможно изменить login. User c login - {} уже имеет ID - {}",
                        loginUser.getLogin(),
                        loginUser.getId());
                throw new FilmNameAlreadyExistException(
                        String.format("Невозможно изменить login. User c login - %s уже имеет ID - %s",
                                loginUser.getLogin(),
                                loginUser.getId()));
            }

        } catch (EmptyResultDataAccessException e) { // эта ошибка обрабатывает NULL на выходе из базы,
            // иначе не нашел как мне получить или null или значение.
            log.info("User с логином {} отсутствует!", user.getLogin());
        }

        try {
            User emailUser = jdbcTemplate.queryForObject(format(""
                    + "SELECT user_id, email, login, name, birthday "
                    + "FROM users "
                    + "WHERE name= '%s'", user.getEmail()), new UserMapper());

            if (emailUser.getId() != user.getId()) {
                log.error("Невозможно изменить email. User c email - {} уже имеет ID - {}",
                        emailUser.getEmail(),
                        emailUser.getId());
                throw new FilmNameAlreadyExistException(
                        String.format("Невозможно изменить email. User c email - %s уже имеет ID - %s",
                                emailUser.getName(),
                                emailUser.getId()));
            }

        } catch (EmptyResultDataAccessException e) {
            log.info("User с email {} отсутствует!", user.getEmail());
        }

        if (user.getName().isBlank()) {
            log.debug("User не имеет имени");
            user.setName(user.getLogin());
        }


    }
}
