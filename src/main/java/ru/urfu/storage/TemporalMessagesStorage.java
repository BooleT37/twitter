package ru.urfu.storage;

import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.annotation.PostConstruct;
import java.util.*;

public class TemporalMessagesStorage implements MessagesStorage {

	private Map<Long, Message> messages;
	private Long lastMessageId = null;

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
    public List<Message> getAllMessages() {
		return new ArrayList(messages.values());
    }

    private Long createUniqIdForMessage() {
		if (lastMessageId == null || messages.containsKey(lastMessageId + 1)) {
			Optional<Long> lastId = messages.keySet().stream().max(Comparator.naturalOrder());
			lastMessageId = lastId.isPresent() ? lastId.get() : -1L;
			return lastMessageId + 1;
		}
		return lastMessageId + 1;
    }

	@Override
    public Message addMessage(Message message) {
        Long id = this.createUniqIdForMessage();
		Message newMessage = new Message(id, message.getContent());
		message.setId(id);
        messages.put(id, message);
		lastMessageId++;
        return message;
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
