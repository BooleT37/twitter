package ru.urfu.controllers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.urfu.models.Message;
import ru.urfu.storage.Storage;
import ru.urfu.storage.exceptions.MessageNotFound;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class MessagesRestControllerTest {
    private final String[] contents = new String[] {
            "Первое тест сообщение",
            "Второе тест сообщение",
            "Третье тест сообщение",
            "Четвертое тест сообщение"
    };

    @Mock private Storage storage;
    @InjectMocks private MessagesRestController controller;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Map<Long, Message> messages = new HashMap<Long, Message>() {};
        messages.put(1L, new Message(contents[0]));
        messages.put(2L, new Message(contents[1]));
        messages.put(4L, new Message(contents[2]));
        messages.put(15L, new Message(contents[3]));
        when(storage.getAllMessages()).thenReturn(messages);

        when(storage.getMessageById(1L)).thenReturn(new Message(contents[0]));
        when(storage.getMessageById(3L)).thenThrow(new MessageNotFound(3L));
    }

    @Test
    public void getAllMessages() throws Exception {
        assertArrayEquals(contents, controller.getAllMessages().values().stream().map(Message::getContent).toArray());
    }

    @Test
    public void getMessage() throws Exception {
		ResponseEntity<Message> response1 = controller.getMessage(1L);
        assertEquals(contents[0], response1.getBody().getContent());
        assertEquals(HttpStatus.OK, response1.getStatusCode());

		exception.expect(MessageNotFound.class);
		exception.expectMessage(String.format("No message with such id: %d", 3L));
		ResponseEntity<Message> response2 = controller.getMessage(3L);
		assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }

    @Test
    public void addMessage() throws Exception {
    	Message message = new Message("Ещё одно сообщение");
		controller.addMessage(message);
		verify(storage, atLeastOnce()).addMessageWithUniqId(any(Message.class));
    }

}