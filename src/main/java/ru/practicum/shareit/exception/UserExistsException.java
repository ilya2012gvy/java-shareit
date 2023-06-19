package ru.practicum.shareit.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String massage) {
        super(massage);
    }
}
