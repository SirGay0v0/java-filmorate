package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        if (checkForExistingMpa(mpaId)) {
            return jdbcTemplate.queryForObject(
                    "SELECT * " +
                            "FROM mpa " +
                            "WHERE mpa_id = ?", mpaRowMapper(),
                    mpaId);
        } else return null;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM mpa",
                mpaRowMapper());
    }

    private boolean checkForExistingMpa(int mpaId) {
        SqlRowSet mpaRowSet = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM mpa " +
                        "WHERE mpa_id = ?",
                mpaId);
        return mpaRowSet.next();
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return ((rs, rowNum) ->
                Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build()
        );
    }
}
