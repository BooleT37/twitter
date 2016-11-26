package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import java.util.TreeMap;

public interface Storage {
    Message getMessageById(Long id) throws MessageNotFound;

    TreeMap<Long, Message> getAllMessages();

    Long createUniqIdForMessage();

    Long addMessage(Message message);

    Message deleteMessageById(Long id) throws MessageNotFound;

    boolean isStorageEmpty();

}
