package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageAlreadyExists;
import ru.urfu.storage.exceptions.MessageNotFound;

import java.util.TreeMap;

public interface Storage {
    Message getMessageById(Long id) throws MessageNotFound;

    TreeMap<Long, Message> getAllMessages();

    Long createUniqIdForMessage();

    Long addMessageWithUniqId(Message message);

	void addMessage(Long id, Message message) throws MessageAlreadyExists;

    Message deleteMessageById(Long id) throws MessageNotFound;

    boolean isStorageEmpty();

}
