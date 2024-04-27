package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users")
    private User createUser(@RequestBody @Valid User user) {
        return service.createUser(user);
    }

    @PutMapping("/users")
    private User updateUser(@RequestBody @Valid User user) throws NotFoundException {
        return service.updateUser(user);
    }

    @DeleteMapping("/users/{id}")
    private void deleteUser(@PathVariable long id) {
        service.deleteUser(id);
    }


    @GetMapping("/users")
    private List<User> getUsers() {
        return service.getAllUsersList();
    }

    @GetMapping("/users/{id}")
    private User getUserById(@PathVariable long id) {
        return service.getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    private User addFriends(@PathVariable long id, @PathVariable long friendId) {
        return service.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    private void unfriend(@PathVariable long id, @PathVariable long friendId) {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    private List<User> getFriendsSet(@PathVariable long id) {
        return service.returnFriendList(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    private List<User> getMutualFriendsSet(@PathVariable long id, @PathVariable long otherId) {
        return service.getMutualFriendsList(id, otherId);
    }
}
