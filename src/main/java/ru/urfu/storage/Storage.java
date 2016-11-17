package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.WrongIdException;

import java.util.Map;

public interface Storage {
    Message getMessageById(Long id) throws WrongIdException;

    Map<Long, Message> getAllMessages();

    Long createUniqIdForMessage();

    void addMessageWithUniqId(Message message);

	void addMessage(Long id, Message message) throws WrongIdException;

    Message deleteMessageById(Long id) throws WrongIdException;

    boolean isStorageEmpty();

}
