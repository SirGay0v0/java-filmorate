package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService {
    private Set<Long> userSet;
    private Set<Long> friendSet;

    private void makeFriend(User user, User friend) {
        userSet = user.getFriendsSet();
        userSet.add(friend.getId());
        user.setFriendsSet(userSet);

        friendSet = friend.getFriendsSet();
        friendSet.add(user.getId());
        friend.setFriendsSet(friendSet);
    }

    private void unfriend(User user, User friend) {
        userSet = user.getFriendsSet();
        userSet.remove(friend.getId());
        user.setFriendsSet(userSet);

        friendSet = friend.getFriendsSet();
        friendSet.remove(user.getId());
        friend.setFriendsSet(friendSet);
    }

    private Set<Long> getMutualsFriendSet(User user, User friend) {
        userSet = user.getFriendsSet();
        friendSet = friend.getFriendsSet();
        return userSet.stream()
                .filter(u -> friendSet.contains(u))
                .collect(Collectors.toSet());
    }
}
