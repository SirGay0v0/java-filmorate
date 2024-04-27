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
        if (filmMap.containsKey(film.getId())) {
            filmMap.remove(film.getId());
            filmMap.put(film.getId(), film);
            return film;
        } else return null;
    }

    @Override
    public void delete(long filmId) {
        filmMap.remove(filmId);
    }

    @Override
    public List<Film> getAllFilmsList() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film getFilmById(long id) {
        return filmMap.get(id);
    }

    @Override
    public List<Film> getMostLikableFilmSet(int count) {
        return getAllFilmsList().stream()
                .sorted(Comparator.comparing(Film::sizeOfLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(long userId, long filmId) {
        filmMap.get(filmId).getLikesUsersSet().add(userId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        filmMap.get(filmId).getLikesUsersSet().remove(userId);
    }
}
