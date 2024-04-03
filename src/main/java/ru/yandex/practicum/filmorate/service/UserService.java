package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final UserStorage storage;
    private Set<Long> userSet;
    private Set<Long> friendSet;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }


    public void makeFriend(long userId, long friendId) {
        if (storage.getUserById(userId) != null && storage.getUserById(friendId) != null) {
            userSet = storage.getUserById(userId).getFriendsSet();
            userSet.add(friendId);
            storage.getUserById(userId).setFriendsSet(userSet);

            friendSet = storage.getUserById(friendId).getFriendsSet();
            friendSet.add(userId);
            storage.getUserById(friendId).setFriendsSet(friendSet);
        } else throw new NullPointerException("");
    }


    public void unfriend(long userId, long friendId) {
        userSet = storage.getUserById(userId).getFriendsSet();
        userSet.remove(friendId);
        storage.getUserById(userId).setFriendsSet(userSet);

        friendSet = storage.getUserById(friendId).getFriendsSet();
        friendSet.remove(userId);
        storage.getUserById(friendId).setFriendsSet(friendSet);
    }

    public Set<User> returnFriendSet(long id) {
        return storage.getUserById(id).getFriendsSet().stream()
                .map(storage::getUserById)
                .collect(Collectors.toSet());
    }

    public Set<User> getMutualFriendsSet(long userId, long otherId) {
        userSet = storage.getUserById(userId).getFriendsSet();
        friendSet = storage.getUserById(otherId).getFriendsSet();

        return userSet.stream()
                .filter(id -> friendSet.contains(id))
                .map(storage::getUserById)
                .collect(Collectors.toSet());
    }
}
