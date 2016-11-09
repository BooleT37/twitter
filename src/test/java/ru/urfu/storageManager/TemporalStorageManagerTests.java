package ru.urfu.storageManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.urfu.models.Message;
import ru.urfu.storage.TemporalStorage;
import ru.urfu.storageManager.exceptions.WrongIdException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TemporalStorageManagerTests {
    private final String[] contents = new String[] {
            "Первое тест сообщение",
            "Второе тест сообщение",
            "Третье тест сообщение",
            "Четвертое тест сообщение"
    };

    @Mock(name="storage") private TemporalStorage storage;

    @InjectMocks private TemporalStorageManager manager; //inject storage into storageManager

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        //used in this.getAllMessages
        Map<Long, Message> messages = new HashMap<Long, Message>() {};
        messages.put(1L, new Message(contents[0]));
        messages.put(2L, new Message(contents[1]));
        messages.put(4L, new Message(contents[2]));
        messages.put(15L, new Message(contents[3]));
        when(storage.getMessages()).thenReturn(messages);

        //used in this.addMessage
        when(storage.getMessageById(2L)).thenReturn(new Message(contents[1]));

        //used in this.getMessageById
        when(storage.getMessageById(1L)).thenReturn(new Message(contents[0]));
        when(storage.getMessageById(3L)).thenReturn(null);

        //used in this.deleteMessageById
        when(storage.deleteMessageById(1L)).thenReturn(new Message(contents[0]));
    }

    @Test
    public void getMessageById() throws Exception {
        assertEquals(contents[0], manager.getMessageById(1L).getContent());
        exception.expect(WrongIdException.class);
        exception.expectMessage("No messages with such id: 3");
        manager.getMessageById(3L);
    }

    @Test
    public void getAllMessages() throws Exception {
        assertArrayEquals(contents, manager.getAllMessages().values().stream().map(Message::getContent).toArray());
    }

    @Test
    public void createUniqIdForMessage() throws Exception {
        assertEquals(16L, (long)manager.createUniqIdForMessage());
    }

    @Test
    public void addMessage() throws Exception {
        Message message = new Message("Новое сообщение");
        manager.addMessage(5L, message);

        Message newMessage = new Message("Ещё сообщение");
        exception.expect(WrongIdException.class);
        exception.expectMessage("Message with this id already exists: 2");
        manager.addMessage(2L, newMessage);
    }

    public void addMessageWithUniqId() throws Exception {
        manager.addMessageWithUniqId(new Message("Новое сообщение"));
    }

    @Test
    public void deleteMessageById() throws Exception {
        manager.deleteMessageById(1L);

        exception.expect(WrongIdException.class);
        exception.expectMessage("No messages with such id: 3");
        manager.deleteMessageById(3L);
    }

}