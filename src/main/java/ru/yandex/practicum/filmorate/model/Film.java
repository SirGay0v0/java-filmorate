package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.Validators.ReleaseDateValidator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @ReleaseDateValidator
    private LocalDate releaseDate;
    @Min(1)
    private Long duration;
    private Set<Long> likesUsersSet = new HashSet<>();
    private Set<Genre> genreSet = new HashSet<>();
    private Rating rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    public int sizeOfLikes() {
        return likesUsersSet.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
