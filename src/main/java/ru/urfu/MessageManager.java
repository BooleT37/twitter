package ru.urfu;

import java.util.List;

public interface MessageManager {
    Message getById(Long id) throws WrongIdException;

    List<Message> getAll();

    void add(String content);

    void deleteById(Long id) throws WrongIdException;
}