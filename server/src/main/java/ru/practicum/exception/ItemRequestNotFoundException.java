package ru.practicum.exception;

public class ItemRequestNotFoundException extends RuntimeException {
    public ItemRequestNotFoundException(String massage) {
        super(massage);
    }
}
