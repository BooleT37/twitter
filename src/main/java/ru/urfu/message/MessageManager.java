package ru.urfu.message;

import ru.urfu.message.exceptions.WrongIdException;

import java.util.List;

public interface MessageManager {
    Message getById(Long id) throws WrongIdException;

    List<Message> getAll();

    void add(String content);

    void deleteById(Long id) throws WrongIdException;
}