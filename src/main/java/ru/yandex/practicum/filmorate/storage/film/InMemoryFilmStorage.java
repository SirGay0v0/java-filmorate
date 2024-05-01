package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmMap = new ConcurrentHashMap<>();
    private long id = 1;

    @Override
    public Film create(Film film) {
        film.setId(id);
        filmMap.put(id, film);
        id++;
        return film;
    }

    @Override
    public Film update(Film film) {
        if (checkFilmForExisting(film.getId())) {
            filmMap.remove(film.getId());
            filmMap.put(film.getId(), film);
            return film;
        } else {
            return null;
        }
    }

    @Override
    public List<Film> getAllFilmsList() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film getFilmById(long id) {
        if (checkFilmForExisting(id)) {
            return filmMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public List<Film> getMostLikableFilmList(int count) {
        return getAllFilmsList().stream()
                .sorted(Comparator.comparing(Film::sizeOfLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addLike(long userId, long filmId) {
        if (checkFilmForExisting(filmId)) {
            filmMap.get(filmId).getLikesUsersSet().add(userId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeLike(long userId, long filmId) {
        if (checkFilmForExisting(filmId)) {
            filmMap.get(filmId).getLikesUsersSet().remove(userId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkFilmForExisting(long filmId) {
        return filmMap.containsKey(filmId);
    }
}
