package ru.yandex.practicum.filmorate.Validators;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class UserValidator implements ConstraintValidator<CorrectUser, User> {


    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (user.getLogin().matches(".*\\s.*")) {
            return false;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return !user.getBirthday().isAfter(LocalDate.now());
    }
}