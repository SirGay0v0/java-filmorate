package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.exceptions.InvalidArgumentsRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void addLike(long userId, long filmId) throws NotFoundException;

    void removeLike(long userId, long filmId) throws NotFoundException;

    List<Film> getMostLikableFilmsList(int count);

    Film createFilm(Film film) throws InvalidArgumentsRequestException;

    Film updateFilm(Film film) throws ValidationException;

    List<Film> getListAllFilms();

    Film getFilmById(Integer filmId) throws NotFoundException;

}
