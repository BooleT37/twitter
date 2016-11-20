package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageAlreadyExists;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.*;

@Named
public class TemporalStorage implements Storage {

	private TreeMap<Long, Message> messages;

	@PostConstruct
	void setUp() {
		messages = new TreeMap<>();
		this.addMessageWithUniqId(new Message("Моё первое сообщение"));
		this.addMessageWithUniqId(new Message("Здесь будет новое сообщение :)"));
	}

	/**
	 * For testing purposes
	 */
	void setMessages(TreeMap<Long, Message> messages) {
		this.messages = messages;
	}

	@Override
    public Message getMessageById(Long id) throws MessageNotFound {
        Message message = messages.get(id);
        if (message == null)
            throw new MessageNotFound(id);
        return message;
    }

	@Override
    public Map<Long, Message> getAllMessages() {
        return messages;
    }

	@Override
    public Long createUniqIdForMessage() {
        try {
            Long lastId = messages.lastKey();
            return lastId + 1;
        } catch (NoSuchElementException e) {
            return 0L;
        }
    }

	@Override
	public void addMessage(Long id, Message message) throws MessageAlreadyExists {
        Message messageWithTheSameId = messages.get(id);
        if (messageWithTheSameId != null)
            throw new MessageAlreadyExists(id);
		messages.put(id, message);
    }

	@Override
    public Long addMessageWithUniqId(Message message) {
        Long id = this.createUniqIdForMessage();
        messages.put(id, message);
        return id;
    }

	@Override
    public Message deleteMessageById(Long id) throws MessageNotFound {
        Message deletedMessage = messages.remove(id);
        if (deletedMessage == null)
            throw new MessageNotFound(id);
        return deletedMessage;
    }

	@Override
	public boolean isStorageEmpty() {
		return messages.isEmpty();
	}
}
