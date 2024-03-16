package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Validators.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class FilmController {

    private final Map<Integer, Film> filmMap = new ConcurrentHashMap<>();
    private int id = 1;

    @PostMapping("/films")
    private Film createFilm(@RequestBody @Valid Film film) {
        film.setId(id);
        filmMap.put(id, film);
        id++;
        log.info("Фильм успешно создан");
        return film;
    }

    @PutMapping("/films")
    private Film updateFilm(@RequestBody @Valid Film film) throws ValidationException {
        if (filmMap.containsKey(film.getId())) {
            filmMap.remove(film.getId());
            filmMap.put(film.getId(), film);
            log.info("Фильм успешно обновлен");
            return film;
        } else throw new ValidationException("not exist");
    }

    @GetMapping("/films")
    private List<Film> getFilms() {
        log.info("Возвращен список всех фильмов");
        return filmMap.keySet().stream()
                .map(filmMap::get)
                .collect(Collectors.toList());
    }
}
