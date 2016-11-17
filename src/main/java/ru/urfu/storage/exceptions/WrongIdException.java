package ru.urfu.storage.exceptions;

public class WrongIdException extends StorageManagerException {
    public WrongIdException(String message) {
        super(message);
    }

    //preferable constructor
    public WrongIdException(Long id) {
        this(String.format("No messages with such id: %d", id));
    }

    public WrongIdException() {
        this("No messages with such id");
    }
}
