package ru.practicum.shareit.exception;

public class ItemExistsException extends RuntimeException {
    public ItemExistsException(String message) {
        super(message);
    }
}
