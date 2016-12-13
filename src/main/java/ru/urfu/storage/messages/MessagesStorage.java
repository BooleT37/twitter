package ru.urfu.storage.messages;

import ru.urfu.models.Message;
import ru.urfu.storage.messages.exceptions.MessageNotFound;

import java.util.List;

public interface MessagesStorage {
    Message getById(Long id) throws MessageNotFound;

    List<Message> getAll();

    Message add(Message message);

    Message deleteById(Long id) throws MessageNotFound;

    boolean isEmpty();

}
