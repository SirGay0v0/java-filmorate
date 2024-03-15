package ru.yandex.practicum.filmorate.Validators;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends Exception {
    public ValidationException(String message) {
        log.warn("ошибка проверки поля " + message);
    }
}
