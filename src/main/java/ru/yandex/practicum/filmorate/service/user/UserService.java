package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User addFriend(long userId, long friendId) throws NotFoundException;

    void deleteFriend(long userId, long friendId) throws NotFoundException;

    List<User> returnFriendList(Long id) throws NotFoundException;

    List<User> getMutualFriendsList(long userId, long otherId) throws NotFoundException;

    User createUser(User user);

    User updateUser(User user) throws NotFoundException;

    void deleteUser(long id);

    List<User> getAllUsersList();

    User getUserById(long id);

}