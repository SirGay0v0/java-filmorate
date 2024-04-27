package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO films (film_name, description, release_date, duration_min, film_rating)" +
                            " VALUES (?, ?, ?, ?, ?)",
                    new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getRating().getCode());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films" +
                " SET film_name = ?, description = ?, release_date = ?, duration_min = ?, film_rating = ?" +
                " WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getRating().getCode(), film.getId());
        return film;
    }

    @Override
    public void delete(long filmId) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Film> getAllFilmsList() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films");
        List<Film> filmList = new ArrayList<>();
        while (!filmRows.isAfterLast()) {
            filmRows.next();
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                    filmRows.getLong("duration_min"),
                    Rating.getRatingByCode(filmRows.getInt("film_rating"))
            );
            filmList.add(film);
        }
        return filmList;
    }

    @Override
    public Film getFilmById(long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", filmId);
        if (filmRows.next()) {
            return new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                    filmRows.getLong("duration_min"),
                    Rating.getRatingByCode(filmRows.getInt("film_rating")));
        } else {
            return null;
        }
    }

    @Override
    public List<Film> getMostLikableFilmSet(int count) {
        List<Film> filmList = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT *" +
                        "FROM films AS f" +
                        "INNER JOIN (SELECT film_id, COUNT(DISTINCT user_id) AS condition" +
                        "FROM likes" +
                        "GROUP BY film_id" +
                        "ORDER BY COUNT( DISTINCT user_id) DESC" +
                        "LIMIT ?) AS l ON f.film_id=l.film_id" +
                        "ORDER BY condition DESC", count);
        while (!filmRows.isAfterLast()) {
            filmRows.next();
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                    filmRows.getLong("duration_min"),
                    Rating.getRatingByCode(filmRows.getInt("film_rating")));
            filmList.add(film);
        }
        return filmList;
    }

    @Override
    public void addLike(long userId, long filmId) {
        String sqlAddLke = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlAddLke, filmId, userId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        String sqlRemoveLke = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlRemoveLke, filmId, userId);
    }
}
