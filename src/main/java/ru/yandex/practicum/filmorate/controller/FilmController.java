package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Validators.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    @Autowired
    private InMemoryFilmStorage storage;

    @PostMapping("/films")
    private Film createFilm(@Valid Film film) {
        storage.create(film);
        log.info("Фильм успешно создан");
        return film;
    }

    @PutMapping("/films")
    private Film updateFilm(@Valid Film film) throws ValidationException {
        if (storage.update(film).equals(film)) {
            log.info("Фильм успешно обновлен");
            return film;
        } else throw new ValidationException("not exist");
    }

    @GetMapping("/films")
    private List<Film> getFilms() {
        log.info("Возвращен список всех фильмов");
        return storage.returnAll();
    }
}
