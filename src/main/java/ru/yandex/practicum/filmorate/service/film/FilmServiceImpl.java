package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidArgumentsRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                           @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(long userId, long filmId) throws NotFoundException {
        if (userStorage.getUserById(userId) != null && filmStorage.getFilmById(filmId) != null) {
            filmStorage.addLike(userId, filmId);
            log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + filmId);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void removeLike(long userId, long filmId) throws NotFoundException {
        if (userStorage.getUserById(userId) != null && filmStorage.getFilmById(filmId) != null) {
            filmStorage.removeLike(userId, filmId);
            log.info("Пользователь с id " + userId + " убрал лайк с фильма с id " + filmId);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<Film> getMostLikableFilmsList(int count) {
        log.info("Возвращен список из " + count + " фильмов с наибольшем коичеством лайков");
        return filmStorage.getMostLikableFilmList(count);
    }

    @Override
    public Film createFilm(Film film) throws InvalidArgumentsRequestException {
        Film filmCreated = filmStorage.create(film);
        if (filmCreated != null) {
            log.info("Фильм c id " + filmCreated.getId() + " создан");
            return filmCreated;
        } else {
            throw new InvalidArgumentsRequestException();
        }
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (filmStorage.update(film).equals(film)) {
            log.info("Фильм c id " + film.getId() + " обновлен");
            return film;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public List<Film> getListAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.getAllFilmsList();
    }

    @Override
    public Film getFilmById(Integer filmId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            log.info("Возвращен фильм с жанром.");
            return film;
        } else {
            throw new NotFoundException();
        }
    }
}

