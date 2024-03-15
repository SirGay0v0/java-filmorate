package ru.yandex.practicum.filmorate.Validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UserValidator.class})
public @interface CorrectUser {
    String message() default "{Invalid User}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
