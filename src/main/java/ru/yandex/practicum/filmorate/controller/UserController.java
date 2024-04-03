package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ErrorHandler;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
public class UserController {

    private final UserStorage storage;
    private final UserService service;

    @Autowired
    public UserController(UserStorage storage, UserService service, ErrorHandler handler) {
        this.storage = storage;
        this.service = service;
    }

    @PostMapping("/users")
    private User createUser(@RequestBody @Valid User user) {
        storage.create(user);
        log.info("Пользователь " + user.getId() + " создан!");
        return user;
    }

    @PutMapping("/users")
    private User updateUser(@RequestBody @Valid User user) throws NotFoundException {
        if (storage.update(user) != null) {
            log.info("Пользователь" + user.getId() + " обновлен");
            return user;
        } else throw new NotFoundException(user.toString());
    }

    @GetMapping("/users")
    private Set<User> getUsers() {
        log.info("Возвращен список всех пользователей");
        return storage.returnAll();
    }

    @GetMapping("/users/{id}")
    private User getUserById(@PathVariable long id) {
        log.info("Вовращен пользователь с id " + id);
        return storage.getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    private void makeFriends(@PathVariable long id, @PathVariable long friendId) {
        service.makeFriend(id, friendId);
        log.info(storage.getUserById(id).getName() + " и " +
                storage.getUserById(friendId).getName() + " теперь друзья.");
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    private void unfriend(@PathVariable long id, @PathVariable long friendId) {
        service.unfriend(id, friendId);
        log.info(storage.getUserById(id).getName() + " и " +
                storage.getUserById(friendId).getName() + " больше не друзья.");
    }

    @GetMapping("/users/{id}/friends")
    private Set<User> getfriendsSet(@PathVariable long id) {
        log.info("Возвращен список друзей пользователя с id " + id);
        return service.returnFriendSet(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    private Set<User> getMutualfriendsSet(@PathVariable long id, @PathVariable long otherId) {
        log.info("Возвращен список общих друзей пользователей с id " + id + " и " + otherId);
        return service.getMutualFriendsSet(id, otherId);
    }
}
