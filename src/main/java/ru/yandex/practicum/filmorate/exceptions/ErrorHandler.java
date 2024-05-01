package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = {NotFoundException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound() {
        return new ErrorResponse(
                "Ошибка с поиском объекта",
                "Искомый объект не существет");
    }

    @ExceptionHandler(value = {InvalidArgumentsRequestException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError() {
        return new ErrorResponse(
                "Ошибка с полем объекта",
                "поля объекта содержат ошибки");
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAnyException(IOException e) {
        return new ErrorResponse(
                "непредвиденная ошибка",
                e.getMessage());
    }
}
