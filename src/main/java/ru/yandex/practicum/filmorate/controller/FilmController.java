package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {

    private final FilmService service;

    @Autowired
    public FilmController(InMemoryFilmStorage storage, FilmService service) {
        this.service = service;
    }

    @PostMapping("/films")

    private Film createFilm(@RequestBody @Valid Film film) {
        return service.createFilm(film);
    }

    @PutMapping("/films")
    private Film updateFilm(@RequestBody @Valid Film film) throws ValidationException {
        return service.updateFilm(film);
    }

    @GetMapping("/films")
    private List<Film> getFilms() {
        return service.getListAllFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    private void likeFilm(@PathVariable long id, @PathVariable long userId) {
        service.addLike(userId, id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    private void removeLike(@PathVariable long id, @PathVariable long userId) {
        service.removeLike(userId, id);
    }

    @GetMapping("/films/popular")
    private List<Film> getMostLikableFilms(@RequestParam(defaultValue = "10") String count) {
        return service.getMostLikableFilmsList(Integer.parseInt(count));
    }
}
