package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaService {

    Mpa getMpaById(Integer mpaId) throws NotFoundException;

    List<Mpa> getAllMpa();
}
