package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(@Qualifier("inMemoryUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    public User addFriend(long userId, long friendId) {
        log.info("Пользователь с id " + userId + " добавил в друзья пользователя с id " + friendId + " .");
        return storage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        storage.deleteFriend(userId, friendId);
        log.info("Пользователи с id " + userId + " и " + friendId + " больше не друзья.");
    }

    public List<User> returnFriendList(long id) {
        log.info("Возвращен список друзей пользователя с id " + id);
        return storage.getFriendsList(id);
    }

    public List<User> getMutualFriendsList(long userId, long otherId) {
        log.info("Возвращен список общих друзей пользователей с id " + userId + " и " + otherId);
        return storage.getMutualFriendsList(userId, otherId);
    }

    public User createUser(User user) {
        storage.create(user);
        log.info("Пользователь " + user.getId() + " создан!");
        return user;
    }

    public User updateUser(User user) throws NotFoundException {
        if (storage.update(user) != null) {
            log.info("Пользователь" + user.getId() + " обновлен");
            return user;
        } else throw new NotFoundException();
    }

    public void deleteUser(long id) {
        storage.delete(id);
        log.info("Пользователь" + id + " удален.");
    }

    public List<User> getAllUsersList() {
        log.info("Возвращен список всех пользователей");
        return storage.getAllUsersList();
    }

    public User getUserById(long id) {
        log.info("Вовращен пользователь с id " + id);
        return storage.getUserById(id);
    }
}
