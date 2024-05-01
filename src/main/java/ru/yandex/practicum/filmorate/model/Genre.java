package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genre {
    private int id;
    private String name;

    public static Genre.Builder builder() {
        return new Genre().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Genre.Builder id(int id) {
            Genre.this.id = id;
            return this;
        }

        public Genre.Builder name(String name) {
            Genre.this.name = name;
            return this;
        }

        public Genre build() {
            return Genre.this;
        }
    }
}
