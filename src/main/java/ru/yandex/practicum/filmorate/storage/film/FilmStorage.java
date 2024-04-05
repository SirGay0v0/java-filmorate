package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void create(Film film);

    Film update(Film film);

    void delete(Film film);

    List<Film> returnAll();

    Film getFilmById(long id);

    List<Film> getMostLikableFilmSet(int count);
}
