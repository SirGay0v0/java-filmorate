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
    public void delete(long userId) {
        userMap.remove(userId);
    }

    @Override
    public List<User> getAllUsersList() {
        return userMap.keySet().stream()
                .map(userMap::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFriendsList(long id) {
        return getUserById(id).getFriendsSet().stream()
                .map(userMap::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getMutualFriendsList(long userId, long otherId) {
        return getUserById(userId).getFriendsSet().stream()
                .filter(id -> getUserById(otherId).getFriendsSet().contains(id))
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        userMap.get(userId).getFriendsSet().add(friendId);
        userMap.get(friendId).getFriendsSet().add(userId);
        return userMap.get(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        userMap.get(userId).getFriendsSet().remove(friendId);
        userMap.get(friendId).getFriendsSet().remove(userId);
    }

    @Override
    public User getUserById(long id) {
        return userMap.get(id);
    }
}
