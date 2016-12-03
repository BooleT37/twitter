package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import java.util.List;

public interface MessagesStorage {
    Message getMessageById(Long id) throws MessageNotFound;

    List<Message> getAllMessages();

    Message addMessage(Message message);

    Message deleteMessageById(Long id) throws MessageNotFound;

    boolean isStorageEmpty();

}
