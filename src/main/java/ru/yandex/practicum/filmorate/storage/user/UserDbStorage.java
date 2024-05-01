package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(Long userId) {
        if (checkUserForExisting(userId)) {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE user_id = ?",
                    userRowMapper(), userId);
        }
        return null;
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
        if (checkUserForExisting(user.getId())) {
            jdbcTemplate.update("UPDATE users " +
                            "SET email = ?, login = ?, user_name = ?, birthday = ? " +
                            "WHERE user_id = ?",
                    user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void delete(Long userId) {
        if (checkUserForExisting(userId)) {
            jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);
        }
    }


    @Override
    public List<User> getAllUsersList() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper());
    }

    @Override
    public List<User> getFriendsList(Long userId) {
        if (checkUserForExisting(userId)) {
            return jdbcTemplate.query(
                    "SELECT * " +
                            "FROM users " +
                            "WHERE user_id IN " +
                            "(SELECT DISTINCT friend_id " +
                            "FROM friends " +
                            "WHERE user_id = ?)", userRowMapper(), userId);
        } else {
            return null;
        }
    }

    @Override
    public List<User> getMutualFriendsList(Long userId, Long otherId) {
        if (checkUserForExisting(userId) && checkUserForExisting(otherId)) {
            return jdbcTemplate.query(
                    "select * from users where user_id IN(" +
                            "select friend_id from friends where user_id = ? AND friend_id IN(" +
                            "select friend_id from friends where user_id = ?))",
                    userRowMapper(), otherId, userId);
        }
        return null;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        if (checkUserForExisting(userId) && checkUserForExisting(friendId)) {
            jdbcTemplate.update(
                    "insert into friends (user_id, friend_id) values (?, ?)",
                    userId, friendId);
            return getUserById(userId);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
        if (checkUserForExisting(userId) && checkUserForExisting(friendId)) {
            jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?",
                    userId, friendId);
            return true;
        } else {
            return false;
        }
    }

    private List<Long> setFriendsListFromDb(Long id) {
        List<Long> friends = new ArrayList<>();
        String sql = "select friend_id from friends where user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        while (sqlRowSet.next()) {
            friends.add(sqlRowSet.getLong("friend_id"));
        }
        return friends;
    }


    private RowMapper<User> userRowMapper() {
        return ((rs, rowNum) ->
                User.builder()
                        .id(rs.getLong("user_id"))
                        .name(rs.getString("user_name"))
                        .email(rs.getString("email"))
                        .login(rs.getString("login"))
                        .birthday(rs.getDate("birthday").toLocalDate())
                        .friendsList(setFriendsListFromDb(rs.getLong("user_id")))
                        .build()
        );
    }

    @Override
    public boolean checkUserForExisting(Long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM users " +
                        "WHERE user_id = ?", userId);
        return userRows.next();
    }
}
