package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int genreId) {
        if (checkForExistingGenre(genreId)) {
            return jdbcTemplate.queryForObject(
                    "SELECT * " +
                            "FROM genre " +
                            "WHERE genre_id = ?", genreRowMapper(),
                    genreId);
        } else return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * " +
                "FROM genre", genreRowMapper());
    }

    private boolean checkForExistingGenre(int genreId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM genre " +
                        "WHERE genre_id = ?", genreId);
        return filmRows.next();
    }

    private RowMapper<Genre> genreRowMapper() {
        return ((rs, rowNum) ->
                Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre_name"))
                        .build()
        );
    }
}
