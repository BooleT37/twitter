package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.NoSuchElementException;
import java.util.TreeMap;

@Named
public class TemporalStorage implements Storage {

	private TreeMap<Long, Message> messages;

	TemporalStorage() {}

	/**
	 * For testing purposes
	 */
	TemporalStorage(TreeMap<Long, Message> messages) {
		this.messages = messages;
	}

	@PostConstruct
	void setUp() {
		messages = new TreeMap<>();
		this.addMessage(new Message("Моё первое сообщение"));
		this.addMessage(new Message("Здесь будет новое сообщение :)"));
	}


	@Override
    public Message getMessageById(Long id) throws MessageNotFound {
        Message message = messages.get(id);
        if (message == null)
            throw new MessageNotFound(id);
        return message;
    }

	@Override
    public TreeMap<Long, Message> getAllMessages() {
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
    public Long addMessage(Message message) {
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
