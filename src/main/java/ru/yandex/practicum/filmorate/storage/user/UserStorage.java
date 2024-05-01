package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(Long userId);

    User getUserById(Long userId);

    List<User> getAllUsersList();

    List<User> getFriendsList(Long id);

    List<User> getMutualFriendsList(Long userId, Long otherId);

    User addFriend(Long userId, Long friendId);

    boolean deleteFriend(Long userId, Long friendId);

    boolean checkUserForExisting(Long userId);
}
