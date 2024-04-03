package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage storage;
    private Set<Long> filmLikesSet;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage storage, UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    public void addLike(long userId, long filmId) {
        filmLikesSet = storage.getFilmById(filmId).getLikesUsersSet();
        filmLikesSet.add(userStorage.getUserById(userId).getId());
        storage.getFilmById(filmId).setLikesUsersSet(filmLikesSet);
    }

    public void removeLike(long userId, long filmId) {
        filmLikesSet = storage.getFilmById(filmId).getLikesUsersSet();
        filmLikesSet.remove(userStorage.getUserById(userId).getId());
        storage.getFilmById(filmId).setLikesUsersSet(filmLikesSet);
    }

    public List<Film> getMostLikableFilmsSet(int count) {
        return storage.returnAll().stream()
                .sorted(Comparator.comparing(Film::sizeOfLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
