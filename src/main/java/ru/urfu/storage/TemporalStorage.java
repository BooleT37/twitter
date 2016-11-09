package ru.urfu.storage;

import ru.urfu.models.Message;

import javax.inject.Named;
import java.util.Map;
import java.util.TreeMap;

@Named
public class TemporalStorage {

    private Map<Long, Message> messages;

    private TemporalStorage() {
        messages = new TreeMap<>();
        messages.put(1L, new Message("Моё первое сообщение"));
        messages.put(2L, new Message("Здесь будет новое сообщение :)"));
    }

    public Map<Long, Message> getMessages() {
        return messages;
    }

    public Message getMessageById(Long id) { return messages.get(id); }

    public void addMessage(Long id, Message message ) {
        messages.put(id, message);
    }

    public Message deleteMessageById(Long id) { return messages.remove(id); }
}
