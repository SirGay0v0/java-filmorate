package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Rating {
    G(1),
    PG(2),
    PG_13(3),
    R(4),
    NC_17(5);

    private final int code;

    Rating(int code) {
        this.code = code;
    }

    private static final Map<Integer, Rating> RATING_MAP = new HashMap<>();

    static {
        for (Rating rate : values()) {
            RATING_MAP.put(rate.code, rate);
        }
    }

    public static Rating getRatingByCode(int code) {
        return RATING_MAP.get(code);
    }
}