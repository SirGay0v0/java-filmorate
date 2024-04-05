package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void create(User user);

    User update(User user);

    void delete(User user);

    User getUserById(long userId);

    List<User> returnAll();

    List<User> getFriendsList(long id);

    List<User> getMutualFriendsList(long userId, long otherId);

    long checkForExistingUsers(long userId, long friendId);
}
