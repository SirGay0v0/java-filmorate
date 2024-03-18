package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Validators.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {

    private final Map<Integer, User> userMap = new ConcurrentHashMap<>();
    private int id = 1;

    @PostMapping("/users")
    private User createUser(@RequestBody @Valid User user) {
        user.setId(id);
        userMap.put(id, user);
        id++;
        log.info("Пользователь успешно создан");
        return user;
    }

    @PutMapping("/users")
    private User updateUser(@RequestBody @Valid User user) throws ValidationException {
        if (userMap.containsKey(user.getId())) {
            userMap.remove(user.getId());
            userMap.put(user.getId(), user);
            log.info("Пользователь успешно обновлен");
            return user;
        } else throw new ValidationException("id");
    }

    @GetMapping("/users")
    private List<User> getUsers() {
        log.info("Возвращен список всех пользователей");
        return userMap.keySet().stream()
                .map(userMap::get)
                .collect(Collectors.toList());
    }
}
