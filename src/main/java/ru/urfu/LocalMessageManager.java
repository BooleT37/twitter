package ru.urfu;

import java.util.List;
import java.util.ArrayList;

public class LocalMessageManager implements MessageManager {

    private static List<Message> _messages;
    private Long _lastId;

    LocalMessageManager() {
        _messages = new ArrayList<>();
        _messages.add(new Message(1, "Моё первое сообщение"));
        _messages.add(new Message(2, "Здесь будет новое сообщение :)"));
    }

    public Message getById(Long id) throws WrongIdException {
        for (Message msg : _messages) {
            if (msg.getId() == id)
                return msg;
        }
        throw new WrongIdException("No message with such id");
    }

    public List<Message> getAll() {
        return _messages;
    }

    public void add(String content) {
        Long nextId = ++_lastId;
        _messages.add(new Message(nextId, content));
    }

    public void deleteById(Long id) throws WrongIdException {
        for (Message msg : _messages) {
            if (msg.getId() == id) {
                _messages.remove(msg);
                return;
            }
        }
        throw new WrongIdException("No message with such id");
    }
}

