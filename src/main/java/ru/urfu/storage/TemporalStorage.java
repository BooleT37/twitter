package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.WrongIdException;

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
    public Message getMessageById(Long id) throws WrongIdException {
        Message message = messages.get(id);
        if (message == null)
            throw new WrongIdException(id);
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
	public void addMessage(Long id, Message message) throws WrongIdException {
        Message messageWithTheSameId = messages.get(id);
        if (messageWithTheSameId != null)
            throw new WrongIdException(String.format("Message with this id already exists: %d", id));
		messages.put(id, message);
    }

	@Override
    public void addMessageWithUniqId(Message message) {
        Long id = this.createUniqIdForMessage();
        messages.put(id, message);
    }

	@Override
    public Message deleteMessageById(Long id) throws WrongIdException {
        Message deletedMessage = messages.remove(id);
        if (deletedMessage == null)
            throw new WrongIdException(id);
        return deletedMessage;
    }

	@Override
	public boolean isStorageEmpty() {
		return messages.isEmpty();
	}
}
