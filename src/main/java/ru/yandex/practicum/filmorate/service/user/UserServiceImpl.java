package ru.yandex.practicum.filmorate.service.user;

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
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Autowired
    public UserServiceImpl(@Qualifier("userDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public User addFriend(long userId, long friendId) throws NotFoundException {
        User user = storage.addFriend(userId, friendId);
        if (user != null) {
            log.info("Пользователь с id " + userId + " добавил в друзья пользователя с id " + friendId + " .");
            return user;
        } else throw new NotFoundException();
    }

    @Override
    public void deleteFriend(long userId, long friendId) throws NotFoundException {
        if (storage.deleteFriend(userId, friendId)) {
            log.info("Пользователи с id " + userId + " и " + friendId + " больше не друзья.");
        } else throw new NotFoundException();

    }

    @Override
    public List<User> returnFriendList(Long id) throws NotFoundException {
        List<User> list = storage.getFriendsList(id);
        if (list != null) {
            log.info("Возвращен список друзей пользователя с id " + id);
            return list;
        } else throw new NotFoundException();
    }

    @Override
    public List<User> getMutualFriendsList(long userId, long otherId) throws NotFoundException {
        List<User> list = storage.getMutualFriendsList(userId, otherId);
        if (list != null) {
            log.info("Возвращен список общих друзей пользователей с id " + userId + " и " + otherId);
            return list;
        } else throw new NotFoundException();
    }

    @Override
    public User createUser(User user) {
        storage.create(user);
        log.info("Пользователь " + user.getId() + " создан!");
        return user;
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        if (storage.update(user) != null) {
            log.info("Пользователь" + user.getId() + " обновлен");
            return user;
        } else throw new NotFoundException();
    }

    @Override
    public void deleteUser(long id) {
        storage.delete(id);
        log.info("Пользователь" + id + " удален.");
    }

    @Override
    public List<User> getAllUsersList() {
        log.info("Возвращен список всех пользователей");
        return storage.getAllUsersList();
    }

    @Override
    public User getUserById(long id) {
        log.info("Вовращен пользователь с id " + id);
        return storage.getUserById(id);
    }
}
