package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> getAllFilmsList();

    Film getFilmById(long id);

    List<Film> getMostLikableFilmList(int count);

    boolean addLike(long userId, long filmId);

    boolean removeLike(long userId, long filmId);

    boolean checkFilmForExisting(long filmId);
}
