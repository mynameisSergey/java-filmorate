package ru.yandex.practicum.filmorate.storage.dao.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.notfound.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.notfound.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exeption.validate.FriendAlreadyExistException;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FriendDbDao implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int userId, int friendId) {
        log.info("запрос на добавления в друзья от user_id={} к friend_id={}", userId, friendId);
        if (userId == friendId) {
            log.info("нельзя добавить самого себя в друзья");
            throw new FriendAlreadyExistException("нельзя добавить самого себя в друзья");
        }
        switch (checkFriend(userId, friendId)) {
            case 1:
            case 3:
                log.info("user_id={} уже отправлял заявку friend_id={}", userId, friendId);
                throw new FriendAlreadyExistException(format("user_id=%d уже отправлял заявку friend_id=%d",
                        userId, friendId));
            case 2:
            case 4:
                log.info("user_id={} одобрил заявку friend_id={}", userId, friendId);
                jdbcTemplate.update("UPDATE friends " +
                        "SET status_user=? " +
                        "WHERE user_id=? " +
                        "AND friend_id=? ", true, userId, friendId);
                break;
            case 5:
                log.info("user_id={} отправил заявку friend_id={}", userId, friendId);
                jdbcTemplate.update("INSERT INTO friends (user_id, friend_id, status_user, status_friend) " +
                        "VALUES(?, ?, ?, ?)", userId, friendId, true, false);
                break;
            default:
                throw new FilmNotFoundException("неизвестная команда добавления фильма");
        }

    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        log.info("запрос на удаление из друзей от user_id={} к friend_id={}", userId, friendId);
        switch (checkFriend(userId, friendId)) {
            case 1:
                log.info("user_id={}, удалил заявку id={}", userId, friendId);
                jdbcTemplate.update(""
                        + "UPDATE friends "
                        + "SET status_user = false "
                        + "WHERE user_id=? "
                        + "AND friend_id=?", userId, friendId);
                break;
            case 3:
                log.info("user_id={}, удалил заявку id={}", userId, friendId);
                jdbcTemplate.update(""
                        + "UPDATE friends "
                        + "SET status_friend = false "
                        + "WHERE user_id=? "
                        + "AND friend_id=?", friendId, userId);
                break;

            case 2:
            case 4:
            case 5:
                log.info("user_id={} не был другом с id={}", userId, friendId);
                throw new FriendNotFoundException(format("user_id=%d не был другом с id=%d", userId, friendId));
            default:
                throw new FriendNotFoundException("неизвестная команда добавления фильма");
        }

    }

    @Override
    public List<Integer> showFriendsById(int userId) {
        log.info("запрос на вывод друзей пользователя ID - {}", userId);

        List<Integer> users = jdbcTemplate.queryForList(format(""
                + "SELECT friend_id "
                + "FROM friends "
                + "WHERE user_id=%d AND status_user=%b ", userId, true), Integer.class);

        List<Integer> friends = jdbcTemplate.queryForList(format(""
                + "SELECT user_id "
                + "FROM friends "
                + "WHERE friend_id=%d AND status_friend=%b ", userId, true), Integer.class);

        users.addAll(friends);

        return users;
    }

    private byte checkFriend(int userId, int friendId) {
        log.info("проверка записи друзей");
        try {

            Boolean userFriendStatus = jdbcTemplate.queryForObject(format(""
                    + "SELECT status_user "
                    + "FROM friends "
                    + "WHERE user_id=%d "
                    + "AND friend_id=%d ", userId, friendId), Boolean.class);
            log.debug("user_id: {}, уже добавил friend_id: {}", userId, friendId);


            if (userFriendStatus) {
                log.debug("user_id:{} и friend_id:{} уже являются друзьями", userId, friendId);
                return 1;
            } else {
                log.debug("user_id:{} отправил заявку friend_id:{}", userId, friendId);
                return 2;
            }
        } catch (EmptyResultDataAccessException ignored) {
            log.info("Сведения о статусе отсутствуют.");
        }

        try {
            Boolean friendUserStatus = jdbcTemplate.queryForObject(format(""
                    + "SELECT status_friend "
                    + "FROM friends "
                    + "WHERE user_id=%d "
                    + "AND friend_id=%d ", friendId, userId), Boolean.class);

            if (friendUserStatus) {
                log.debug("user_id:{} отправил заявку friend_id:{}", userId, friendId);
                return 3;
            } else {
                log.debug("user_id:{} и friend_id:{} уже не являются друзьями", userId, friendId);
                return 4;
            }
        } catch (EmptyResultDataAccessException e) {
            log.info("Данной заявки еще не существует");
            return 5;
        }

    }
}