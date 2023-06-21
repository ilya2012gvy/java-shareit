package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleInvalidEmailException(final ValidationException e) {
        log.info("Ошибка с полем \"%s\": {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleItemNotFoundException(final ItemNotFoundException e) {
        log.info("Произошла ошибка с предметами: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleUserNotFoundException(final UserNotFoundException e) {
        log.info("Произошла ошибка на стороне пользователя: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleItemExistsException(final ItemExistsException e) {
        log.info("Предмет уже существует!: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleUserExistsFoundException(final UserExistsException e) {
        log.info("Пользователь уже существует!: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleThrowable(final Throwable e) {
        log.error("Произошла непредвиденная ошибка: {}", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    RestErrorResponse
    handleException(MethodArgumentNotValidException ex) {

        String message = ex.getFieldErrors()
                .stream()
                .map(e -> " Field " + e.getField() +
                        " Message " + e.getDefaultMessage())
                .reduce("Errors found:", String::concat);
        return new RestErrorResponse(HttpStatus.BAD_REQUEST.value(),
                message, LocalDateTime.now());
    }
}