package ru.urfu.storage.exceptions;

public class MessageNotFound extends StorageManagerException {
    private MessageNotFound(String message) {
        super(message);
    }

    //preferable constructor
    public MessageNotFound(Long id) {
        this(String.format("No message with such id: %d", id));
    }

    public MessageNotFound() {
        this("No message with such id");
    }
}

