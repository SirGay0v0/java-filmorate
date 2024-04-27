package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", userId);
        if (userRows.next()) {
            return new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());
        } else {
            return null;
        }
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (email, login, user_name, birthday) " +
                            "VALUES (?, ?, ?, ?)",
                    new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users " +
                "SET email = ?, login = ?, user_name = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void delete(long userId) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, userId);
    }


    @Override
    public List<User> getAllUsersList() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        List<User> userList = new ArrayList<>();
        while (!userRows.isAfterLast()) {
            userRows.next();
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());
            userList.add(user);
        }
        return userList;
    }

    @Override
    public List<User> getFriendsList(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT * from users " +
                        "where user_id IN " +
                        "(SELECT DISTINCT friend_id " +
                        "FROM friends " +
                        "WHERE user_id = ?)", id);
        List<User> userList = new ArrayList<>();
        while (!userRows.isAfterLast()) {
            userRows.next();
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());
            userList.add(user);
        }
        return userList;
    }

    @Override
    public List<User> getMutualFriendsList(long userId, long otherId) {
        List<User> userList = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT *" +
                        "FROM users" +
                        "WHERE user_id IN(" +
                        "select friend_id" +
                        "from friends as fr1" +
                        "where user_id = ? AND friend_id IN (" +
                        "SELECT friend_id " +
                        "FROM friends" +
                        "WHERE user_id = ?))", userId, otherId);
        while (!userRows.isAfterLast()) {
            userRows.next();
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());
            userList.add(user);
        }
        return userList;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        String sqlFriends = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlFriends, userId, friendId);

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM users WHERE user_id=?", userId);

        return new User(
                userRows.getInt("user_id"),
                userRows.getString("email"),
                userRows.getString("login"),
                userRows.getString("user_name"),
                Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sqlFriends = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlFriends, userId, friendId);
    }
}
