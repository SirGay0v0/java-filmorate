package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(long userId);

    User getUserById(long userId);

    List<User> getAllUsersList();

    List<User> getFriendsList(long id);

    List<User> getMutualFriendsList(long userId, long otherId);

    User addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);
}
