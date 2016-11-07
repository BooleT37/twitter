package ru.urfu.storageManager;

import ru.urfu.models.Message;
import ru.urfu.storage.TemporalStorage;
import ru.urfu.storageManager.exceptions.StorageManagerException;
import ru.urfu.storageManager.exceptions.WrongIdException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class TemporalStorageManager implements IStorageManager {
    private final TemporalStorage storage;

    public TemporalStorageManager(TemporalStorage storage) {
        this.storage = storage;
    }

    public Message getMessageById(Long id) throws WrongIdException {
        Message message = storage.getMessageById(id);
        if (message == null)
            throw new WrongIdException(id);
        return message;
    }

    public Map<Long, Message> getAllMessages() {
        return storage.getMessages();
    }

    public Long createUniqIdForMessage() throws StorageManagerException {
        Optional<Long> UniqId = storage.getMessages().keySet().stream().max(Comparator.naturalOrder());
        if (UniqId.isPresent())
            return UniqId.get() + 1;
        throw new StorageManagerException("Cannot create uniq id for messages");
    }

    public void addMessage(Long id, Message message) throws WrongIdException {
        Message messageWithTheSameId = storage.getMessageById(id);
        if (messageWithTheSameId != null)
            throw new WrongIdException(String.format("Message with this id already exists: %d", id));
        storage.addMessage(id, message);
    }

    public void addMessageWithUniqId(Message message) throws StorageManagerException {
        Long id = this.createUniqIdForMessage();
        storage.addMessage(id, message);
    }

    public Message deleteMessageById(Long id) throws WrongIdException {
        Message deletedMessage = storage.deleteMessageById(id);
        if (deletedMessage == null)
            throw new WrongIdException(id);
        return deletedMessage;
    }
}
