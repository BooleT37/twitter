package ru.urfu.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.urfu.models.Message;
import ru.urfu.storage.Storage;
import ru.urfu.storage.exceptions.WrongIdException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
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
        when(storage.getMessageById(3L)).thenThrow(new WrongIdException(3L));
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

		ResponseEntity<Message> response2 = controller.getMessage(3L);
		assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    @Test
    public void addMessage() throws Exception {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getParameter("content")).thenReturn("Ещё одно сообщение");
		controller.addMessage(requestMock);
		verify(storage, atLeastOnce()).addMessageWithUniqId(any(Message.class));
    }

}