package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    void delete(long filmId);

    List<Film> getAllFilmsList();

    Film getFilmById(long id);

    List<Film> getMostLikableFilmSet(int count);

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);
}
