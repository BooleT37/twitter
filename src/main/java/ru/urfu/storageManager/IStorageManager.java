package ru.urfu.storageManager;

import ru.urfu.models.Message;
import ru.urfu.storageManager.exceptions.StorageManagerException;
import ru.urfu.storageManager.exceptions.WrongIdException;

import java.util.Map;

public interface IStorageManager {
    Message getMessageById(Long id) throws WrongIdException;

    Map<Long, Message> getAllMessages();

    Long createUniqIdForMessage() throws StorageManagerException;

    void addMessage(Long id, Message message) throws WrongIdException;

    void addMessageWithUniqId(Message message) throws StorageManagerException;

    Message deleteMessageById(Long id) throws WrongIdException;

}