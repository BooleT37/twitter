package ru.urfu.controllers.rest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.urfu.entities.Message;
import ru.urfu.entities.User;
import ru.urfu.models.MessageJson;
import ru.urfu.storage.messages.MessagesStorage;
import ru.urfu.storage.messages.exceptions.MessageNotFound;
import ru.urfu.storage.users.UsersStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class MessagesRestControllerTest {
    private final User testUser = new User("Test_user", "test_password");

    private final String[] contents = new String[] {
            "Первое тест сообщение",
            "Второе тест сообщение",
            "Третье тест сообщение",
            "Четвертое тест сообщение"
    };

    @Mock private MessagesStorage messagesStorage;
    @Mock private UsersStorage usersStorage;
    @InjectMocks private MessagesRestController controller;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

		List<Message> messages = new ArrayList<Message>() {};
        messages.add(new Message(0L, contents[0], testUser));
        messages.add(new Message(1L, contents[1], testUser));
        messages.add(new Message(2L, contents[2], testUser));
        messages.add(new Message(3L, contents[3], testUser));
        when(messagesStorage.getAll()).thenReturn(messages);

        when(messagesStorage.getById(1L)).thenReturn(new Message(contents[0], testUser));
        when(messagesStorage.getById(3L)).thenThrow(new MessageNotFound(3L));
    }

    @Test
    public void getAllMessages() throws Exception {
        assertArrayEquals(contents, controller.getAllMessages().stream().map(Message::getContent).toArray());
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
        when(usersStorage.getByLogin(testUser.getLogin())).thenReturn(testUser);
        MessageJson message = new MessageJson("Ещё одно сообщение", testUser.getLogin());
		controller.addMessage(message);
		verify(messagesStorage, atLeastOnce()).add(any(Message.class));
    }

}