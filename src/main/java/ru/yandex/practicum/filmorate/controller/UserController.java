package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Validators.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;

@Slf4j
@RestController
public class UserController {

    private final ArrayList<User> userList = new ArrayList<>();
    private int id = 1;

    @PostMapping("/users")
    private User createUser(@RequestBody @Valid User user) {
        user.setId(id);
        userList.add(user);
        id++;
        log.info("Пользователь успешно создан");
        return user;
    }

    @PutMapping("/users")
    private User updateUser(@RequestBody @Valid User user) throws ValidationException {
        if (userList.contains(user)) {
            userList.remove(user);
            userList.add(user);
            log.info("Пользователь успешно обновлен");
            return user;
        } else throw new ValidationException("id");
    }

    @GetMapping("/users")
    private ArrayList<User> getUsers() {
        log.info("Возвращен список всех пользователей");
        return userList;
    }
}
