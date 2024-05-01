package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@RestController
public class MpaController {
    private final MpaService service;

    @Autowired
    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping("/mpa/{id}")
    private Mpa getMpaById(@PathVariable int id) throws NotFoundException {
        return service.getMpaById(id);
    }

    @GetMapping("/mpa")
    private List<Mpa> getMpaById() {
        return service.getAllMpa();
    }
}
