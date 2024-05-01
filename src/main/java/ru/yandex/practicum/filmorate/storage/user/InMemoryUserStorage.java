package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userMap = new ConcurrentHashMap<>();
    private long id = 1;

    @Override
    public User create(User user) {
        user.setId(id);
        userMap.put(id, user);
        id++;
        return user;
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
    public void delete(Long userId) {
        userMap.remove(userId);
    }

    @Override
    public List<User> getAllUsersList() {
        return userMap.keySet().stream()
                .map(userMap::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFriendsList(Long id) {
        return getUserById(id).getFriendsList().stream()
                .map(userMap::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getMutualFriendsList(Long userId, Long otherId) {
        return getUserById(userId).getFriendsList().stream()
                .filter(id -> getUserById(otherId).getFriendsList().contains(id))
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        userMap.get(userId).getFriendsList().add(friendId);
        userMap.get(friendId).getFriendsList().add(userId);
        return userMap.get(userId);
    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
        if (userMap.containsKey(userId) && userMap.containsKey(friendId)) {
            userMap.get(userId).getFriendsList().remove(friendId);
            userMap.get(friendId).getFriendsList().remove(userId);
            return true;
        } else return false;
    }

    @Override
    public boolean checkUserForExisting(Long userId) {
        return userMap.containsKey(userId);
    }

    @Override
    public User getUserById(Long id) {
        return userMap.get(id);
    }
}
