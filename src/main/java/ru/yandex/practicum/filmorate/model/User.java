package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.Validators.CorrectUser;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@CorrectUser
public class User {
    private long id;
    @NotBlank
    @Pattern(regexp = ".*@.*")
    private String email;
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;
    private List<Long> friendsList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder() {
        return new User().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder id(Long id) {
            User.this.id = id;
            return this;
        }

        public Builder email(String email) {
            User.this.email = email;
            return this;
        }

        public Builder login(String login) {
            User.this.login = login;
            return this;
        }

        public Builder name(String name) {
            User.this.name = name;
            return this;
        }

        public Builder birthday(LocalDate birthday) {
            User.this.birthday = birthday;
            return this;
        }

        public Builder friendsList(List<Long> friendsList) {
            User.this.friendsList = friendsList;
            return this;
        }

        public User build() {
            return User.this;
        }
    }
}
