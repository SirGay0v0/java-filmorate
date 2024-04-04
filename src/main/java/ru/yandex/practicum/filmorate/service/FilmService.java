package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long userId, long filmId) {
        filmStorage.getFilmById(filmId).getLikesUsersSet().add(userStorage.getUserById(userId).getId());
        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + filmId);
    }

    public void removeLike(long userId, long filmId) {
        filmStorage.getFilmById(filmId).getLikesUsersSet().remove(userStorage.getUserById(userId).getId());
        log.info("Пользователь с id " + userId + " убрал лайк с фильма с id " + filmId);

    }

    public List<Film> getMostLikableFilmsSet(int count) {
        log.info("Возвращен список из " + count + " фильмов с наибольшем коичеством лайков");
        return filmStorage.returnAll().stream()
                .sorted(Comparator.comparing(Film::sizeOfLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film createFilm(Film film) {
        log.info("Фильм c id " + film.getId() + " создан");
        filmStorage.create(film);
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        if (filmStorage.update(film).equals(film)) {
            log.info("Фильм c id " + film.getId() + " обновлен");
            return film;
        } else throw new ValidationException("not exist");
    }

    public List<Film> getAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.returnAll();
    }


}

