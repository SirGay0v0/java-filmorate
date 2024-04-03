package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private final InMemoryFilmStorage storage;
    private final FilmService service;

    @Autowired
    public FilmController(InMemoryFilmStorage storage, FilmService service) {
        this.storage = storage;
        this.service = service;
    }

    @PostMapping("/films")

    private Film createFilm(@RequestBody @Valid Film film) {
        storage.create(film);
        log.info("Фильм c id " + film.getId() + " создан");
        return film;
    }

    @PutMapping("/films")
    private Film updateFilm(@RequestBody @Valid Film film) throws ValidationException {
        if (storage.update(film).equals(film)) {
            log.info("Фильм c id " + film.getId() + " обновлен");
            return film;
        } else throw new ValidationException("not exist");
    }

    @GetMapping("/films")
    private List<Film> getFilms() {
        log.info("Возвращен список всех фильмов");
        return storage.returnAll();
    }

    @PutMapping("/films/{id}/like/{userId}")
    private void likeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + id);
        service.addLike(userId, id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    private void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id " + userId + " убрал лайк с фильма с id " + id);
        service.removeLike(userId, id);
    }

    @GetMapping("/films/popular")
    private List<Film> getMostLikableFilms(@RequestParam(defaultValue = "10") String count) {
        log.info("Возвращен список из " + count + " фильмов с наибольшем коичеством лайков");
        return service.getMostLikableFilmsSet(Integer.parseInt(count));
    }
}
