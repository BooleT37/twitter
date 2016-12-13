package ru.urfu.storage.messages;

import ru.urfu.models.Message;
import ru.urfu.models.User;
import ru.urfu.storage.messages.exceptions.MessageNotFound;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named
public class TemporalMessagesStorage implements MessagesStorage {

	private Map<Long, Message> messages;
	private Long lastMessageId = null;

	@PostConstruct
	void setUp() {
		messages = new HashMap<>();
		this.add(new Message("Моё первое сообщение"));
		this.add(new Message("Здесь будет новое сообщение :)"));
	}


	@Override
    public Message getById(Long id) throws MessageNotFound {
        Message message = messages.get(id);
        if (message == null)
            throw new MessageNotFound(id);
        return message;
    }

	@Override
    public List<Message> getAll() {
		return new ArrayList<>(messages.values());
    }

	@Override
	public List<Message> getAll(User user) {
		return messages.values().stream().filter(message -> message.getUser().equals(user)).collect(Collectors.toList());
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
    public Message add(Message message) {
        Long id = this.createUniqIdForMessage();
		Message newMessage = new Message(id, message.getContent());
		message.setId(id);
        messages.put(id, message);
		lastMessageId++;
        return message;
    }

	@Override
    public Message deleteById(Long id) throws MessageNotFound {
        Message deletedMessage = messages.remove(id);
        if (Objects.equals(id, lastMessageId))
        	lastMessageId--;
        if (deletedMessage == null)
            throw new MessageNotFound(id);
        return deletedMessage;
    }

	@Override
	public boolean isEmpty() {
		return messages.isEmpty();
	}
}
