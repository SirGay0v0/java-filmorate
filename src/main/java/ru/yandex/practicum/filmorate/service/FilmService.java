package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private InMemoryFilmStorage storage;
    private Set<Long> filmLikesSet;

    private void addLike(User user, Film film) {
        filmLikesSet = film.getLikesUsersSet();
        filmLikesSet.add(user.getId());
    }

    private void removeLike(User user, Film film) {
        filmLikesSet = film.getLikesUsersSet();
        filmLikesSet.remove(user.getId());
    }

    private List<Film> getMostLikableFilmsSet() {
        return storage.returnAll().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikesUsersSet().size()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
