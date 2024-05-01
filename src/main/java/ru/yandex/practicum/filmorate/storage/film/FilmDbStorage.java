package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public Film create(Film film) {
        if ((film.getMpa() == null || checkForExistingMpa(film.getMpa().getId()))
                && (film.getGenres() == null || checkForExistingGenresFromSet(film.getGenres()))) {
            jdbcTemplate.update(
                    "INSERT INTO films (film_name, description, release_date, duration_min)" +
                            "VALUES (?, ?, ?, ?)",
                    film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());

            Long filmId = jdbcTemplate.queryForObject(
                    "SELECT film_id FROM films ORDER BY film_id DESC LIMIT 1",
                    Long.class);

            if (film.getMpa() != null) {
                jdbcTemplate.update(
                        "UPDATE films " +
                                "SET film_mpa = ? " +
                                "WHERE film_id = ?",
                        film.getMpa().getId(), filmId);
            }

            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update(
                            "INSERT INTO film_genre (film_id, genre_id)" +
                                    "VALUES (?, ?) ",
                            filmId, genre.getId());
                }
            }
            if (filmId != null) {
                return getFilmById(filmId);
            } else return null;
        } else return null;
    }

    @Override
    public Film update(Film film) {
        if (checkFilmForExisting(film.getId())) {
            String sqlQuery = "UPDATE films" +
                    " SET film_name = ?, description = ?, release_date = ?, duration_min = ?" +
                    " WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getId());

            if (film.getMpa() != null) {
                jdbcTemplate.update("UPDATE films " +
                        "SET film_mpa = ? " +
                        "WHERE film_id = ?", film.getMpa().getId(), film.getId());
            }

            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) " +
                            "VALUES (?, ?) ", film.getId(), genre.getId());
                }
            }
            return film;
        }
        return null;
    }

    @Override
    public Film getFilmById(long filmId) {
        if (checkFilmForExisting(filmId)) {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM films WHERE film_id = ?",
                    filmRowMapper(), filmId);
        } else return null;
    }

    @Override
    public List<Film> getAllFilmsList() {
        return jdbcTemplate.query("SELECT * FROM films", filmRowMapper());
    }

    @Override
    public List<Film> getMostLikableFilmList(int count) {
        List<Film> list = jdbcTemplate.query(
                "SELECT * " +
                        "FROM films AS f1 " +
                        "Inner join ( " +
                        "SELECT film_id, COUNT(DISTINCT user_id) AS sorting " +
                        "FROM likes " +
                        "GROUP BY film_id  " +
                        ") AS f2 On f1.film_id=f2.film_id " +
                        "ORDER BY sorting " +
                        "LIMIT ?", filmRowMapper(), count);

        list.sort(Comparator.comparing(Film::sizeOfLikes).reversed());
        return list;

    }

    @Override
    public boolean addLike(long userId, long filmId) {
        if (checkFilmForExisting(filmId) && userStorage.checkUserForExisting(userId)) {
            jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
            return true;
        } else return false;
    }

    @Override
    public boolean removeLike(long userId, long filmId) {
        if (checkFilmForExisting(filmId) && userStorage.checkUserForExisting(userId)) {
            jdbcTemplate.update(
                    "DELETE FROM likes WHERE film_id = ? AND user_id = ?",
                    filmId, userId);
            return true;
        } else
            return false;
    }

    @Override
    public boolean checkFilmForExisting(long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM films " +
                        "WHERE film_id = ?", filmId);
        return filmRows.next();
    }

    private boolean checkForExistingMpa(int mpaId) {
        SqlRowSet mpaRowSet = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM mpa " +
                        "WHERE mpa_id = ?",
                mpaId);
        return mpaRowSet.next();
    }

    private List<Long> setLikesUsersFromDb(long filmId) {
        List<Long> likes = new ArrayList<>();
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, filmId);
        while (sqlRowSet.next()) {
            likes.add(sqlRowSet.getLong("user_id"));
        }
        return likes;
    }

    private List<Genre> setGenreFromDb(long filmId) {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM genre " +
                "WHERE genre_id IN (" +
                "   SELECT genre_id " +
                "FROM film_genre " +
                "WHERE film_id = ?)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, filmId);
        while (sqlRowSet.next()) {
            genres.add(Genre.builder()
                    .id(sqlRowSet.getInt("genre_id"))
                    .name(sqlRowSet.getString("genre_name"))
                    .build());
        }
        genres.sort(Comparator.comparing(Genre::getId));
        return genres;
    }

    private Mpa setMpaFromDb(long filmId) {
        String sql = "SELECT * " +
                "FROM mpa " +
                "WHERE mpa_id = (" +
                "SELECT film_mpa " +
                "FROM films " +
                "WHERE film_id = ?)";
        return jdbcTemplate.queryForObject(sql, mpaRowMapper(), filmId);
    }

    private RowMapper<Film> filmRowMapper() {
        return ((rs, rowNum) ->
                Film.builder()
                        .id(rs.getLong("film_id"))
                        .name(rs.getString("film_name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .duration(rs.getLong("duration_min"))
                        .likesUserSet(setLikesUsersFromDb(rs.getLong("film_id")))
                        .genreSet(setGenreFromDb(rs.getLong("film_id")))
                        .mpa(setMpaFromDb(rs.getLong("film_id")))
                        .build()
        );
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return ((rs, rowNum) ->
                Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build()
        );
    }

    private boolean checkForExistingGenresFromSet(List<Genre> genreSet) {
        for (Genre genre : genreSet) {
            SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                    "SELECT * " +
                            "FROM genre " +
                            "WHERE genre_id = ?", genre.getId());
            if (!filmRows.next()) {
                return false;
            }
        }
        return true;
    }
}

