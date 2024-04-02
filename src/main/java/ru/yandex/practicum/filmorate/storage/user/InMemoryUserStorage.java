package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap = new ConcurrentHashMap<>();
    private int id = 1;

    @Override
    public void create(User user) {
        user.setId(id);
        userMap.put(id, user);
        id++;
    }

    @Override
    public User update(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.remove(user.getId());
            userMap.put(user.getId(), user);
            return user;
        } else return null;
    }

    @Override
    public void delete(User user) {
        userMap.remove(user.getId());
    }

    @Override
    public List<User> returnAll() {
        return userMap.keySet().stream()
                .map(userMap::get)
                .collect(Collectors.toList());
    }
}
