package ru.urfu.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.urfu.models.Message;
import ru.urfu.storage.TemporalStorage;
import ru.urfu.storageManager.StorageManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


public class MessagesRestControllerTests {
    private final String[] contents = new String[] {
            "Первое тест сообщение",
            "Второе тест сообщение",
            "Третье тест сообщение",
            "Четвертое тест сообщение"
    };

    @Mock(name="storageManager") private StorageManager storageManager;
    @InjectMocks private MessagesRestController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new MessagesRestController();
        Map<Long, Message> messages = new HashMap<Long, Message>() {};
        messages.put(1L, new Message(contents[0]));
        messages.put(2L, new Message(contents[1]));
        messages.put(4L, new Message(contents[2]));
        messages.put(15L, new Message(contents[3]));
        when(storageManager.getAllMessages()).thenReturn(messages);
    }

    @Test
    public void getAllMessages() throws Exception {
        assertArrayEquals(contents, controller.getAllMessages().values().stream().map(Message::getContent).toArray());
    }

    @Test
    public void getMessage() throws Exception {

    }

    @Test
    public void addMessage() throws Exception {

    }

}