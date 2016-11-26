package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.*;

@Named
public class TemporalStorage implements Storage {

	private Map<Long, Message> messages;
	private Long lastMessageId = null;

	TemporalStorage() {}

	/**
	 * For testing purposes
	 */
	TemporalStorage(Map<Long, Message> messages) {
		this.messages = messages;
	}

	@PostConstruct
	void setUp() {
		messages = new HashMap<>();
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
    public Map<Long, Message> getAllMessages() {
        return messages;
    }

	@Override
    public Long createUniqIdForMessage() {
		if (lastMessageId == null || messages.containsKey(lastMessageId + 1)) {
			Optional<Long> lastId = messages.keySet().stream().max(Comparator.naturalOrder());
			lastMessageId = lastId.isPresent() ? lastId.get() : -1L;
			return lastMessageId + 1;
		}
		return lastMessageId + 1;
    }

	@Override
    public Long addMessage(Message message) {
        Long id = this.createUniqIdForMessage();
        messages.put(id, message);
		lastMessageId++;
        return id;
    }

	@Override
    public Message deleteMessageById(Long id) throws MessageNotFound {
        Message deletedMessage = messages.remove(id);
        if (Objects.equals(id, lastMessageId))
        	lastMessageId--;
        if (deletedMessage == null)
            throw new MessageNotFound(id);
        return deletedMessage;
    }

	@Override
	public boolean isStorageEmpty() {
		return messages.isEmpty();
	}
}
