package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreServiceImpl;

import java.util.List;

@RestController
public class GenreController {

    private final GenreServiceImpl service;

    public GenreController(@Qualifier("genreServiceImpl") GenreServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/genres/{genreId}")
    private Genre getGenreById(@PathVariable int genreId) throws NotFoundException {
        return service.getGenreById(genreId);
    }

    @GetMapping("/genres")
    private List<Genre> getAllGenresList() {
        return service.getAllGenres();
    }
}
