package ru.urfu;

class WrongIdException extends MessageManagerException {
    WrongIdException(String message) {
        super(message);
    }
}
