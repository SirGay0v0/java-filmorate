package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.Validators.CorrectReleaseDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Film.
 */
@Data
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Length(max = 200)
    private String description;
    @CorrectReleaseDate
    private LocalDate releaseDate;
    @Min(1)
    private Long duration;
    private Mpa mpa;
    private List<Genre> genres;
    private List<Long> likesUsersSet;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    public Integer sizeOfLikes() {
        return likesUsersSet.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Film.Builder builder() {
        return new Film().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Film.Builder id(Long id) {
            Film.this.id = id;
            return this;
        }

        public Film.Builder description(String description) {
            Film.this.description = description;
            return this;
        }

        public Film.Builder releaseDate(LocalDate releaseDate) {
            Film.this.releaseDate = releaseDate;
            return this;
        }

        public Film.Builder name(String name) {
            Film.this.name = name;
            return this;
        }

        public Film.Builder duration(Long duration) {
            Film.this.duration = duration;
            return this;
        }

        public Film.Builder likesUserSet(List<Long> likesUserSet) {
            Film.this.likesUsersSet = likesUserSet;
            return this;
        }

        public Film.Builder genreSet(List<Genre> genreSet) {
            Film.this.genres = genreSet;
            return this;
        }

        public Builder mpa(Mpa mpa) {
            Film.this.mpa = mpa;
            return this;
        }

        public Film build() {
            return Film.this;
        }
    }
}
