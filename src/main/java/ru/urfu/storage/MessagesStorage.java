package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import java.util.List;

public interface MessagesStorage {
    Message getById(Long id) throws MessageNotFound;

    List<Message> getAll();

    Message add(Message message);

    Message deleteById(Long id) throws MessageNotFound;

    boolean isEmpty();

}
