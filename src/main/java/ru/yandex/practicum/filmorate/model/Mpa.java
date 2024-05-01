package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mpa {
    private int id;
    private String name;

    public static Mpa.Builder builder() {
        return new Mpa().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Mpa.Builder id(Integer id) {
            Mpa.this.id = id;
            return this;
        }

        public Mpa.Builder name(String name) {
            Mpa.this.name = name;
            return this;
        }

        public Mpa build() {
            return Mpa.this;
        }
    }
}