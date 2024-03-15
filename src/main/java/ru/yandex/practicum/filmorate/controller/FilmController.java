package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Validators.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;

@Slf4j
@RestController
public class FilmController {

    private final ArrayList<Film> filmList = new ArrayList<>();
    private int id = 1;

    @PostMapping("/films")
    private Film createFilm(@RequestBody @Valid Film film) {
        film.setId(id);
        filmList.add(film);
        id++;
        log.info("Фильм успешно создан");
        return film;
    }

    @PutMapping("/films")
    private Film updateFilm(@RequestBody @Valid Film film) throws ValidationException {
        if (filmList.contains(film)) {
            filmList.remove(film);
            filmList.add(film);
            log.info("Фильм успешно обновлен");
            return film;
        } else throw new ValidationException("not exist");
    }

    @GetMapping("/films")
    private ArrayList<Film> getFilms() {
        log.info("Возвращен список всех фильмов");
        return filmList;
    }
}
