package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public Genre getGenreById(int genreId) throws NotFoundException {
        Genre genre = genreStorage.getGenreById(genreId);
        if (genre != null) {
            log.info("Возвращен genre c id " + genreId);
            return genre;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        log.info("Возвращен список genres.");
        return genreStorage.getAllGenres();
    }
}
