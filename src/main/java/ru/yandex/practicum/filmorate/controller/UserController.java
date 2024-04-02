package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Validators.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private InMemoryUserStorage storage;

    @PostMapping("/users")
    private User createUser(@Valid User user) {
        storage.create(user);
        log.info("Пользователь успешно создан!");
        return user;
    }

    @PutMapping("/users")
    private User updateUser(@Valid User user) throws ValidationException {
        if (storage.update(user).equals(user)) {
            log.info("Пользователь успешно обновлен");
            return user;
        } else throw new ValidationException("id");
    }

    @GetMapping("/users")
    private List<User> getUsers() {
        log.info("Возвращен список всех пользователей");
        return storage.returnAll();
    }
}
