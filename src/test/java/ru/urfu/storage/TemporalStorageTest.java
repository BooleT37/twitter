package ru.urfu.storage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;
import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageAlreadyExists;
import ru.urfu.storage.exceptions.MessageNotFound;

import java.util.TreeMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TemporalStorageTest {
    private final String[] contents = new String[] {
            "Первое тест сообщение",
            "Второе тест сообщение",
            "Третье тест сообщение",
            "Четвертое тест сообщение"
    };

	private TemporalStorage storage;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
        //System.out.println("Running TemporalStorage tests...");

		TreeMap<Long, Message> messages = new TreeMap<>();
        messages.put(1L, new Message(contents[0]));
        messages.put(2L, new Message(contents[1]));
        messages.put(4L, new Message(contents[2]));
        messages.put(15L, new Message(contents[3]));

        storage = new TemporalStorage();
		storage.setUp();
        storage.setMessages(messages);
    }

    @Test
    public void getMessageById() throws Exception {
        assertEquals(contents[0], storage.getMessageById(1L).getContent());
        exception.expect(MessageNotFound.class);
        exception.expectMessage("No message with such id: 3");
        storage.getMessageById(3L);
    }

    @Test
    public void getAllMessages() throws Exception {
        assertArrayEquals(contents, storage.getAllMessages().values().stream().map(Message::getContent).toArray());
    }

    @Test
    public void createUniqIdForMessage() throws Exception {
        assertEquals(16L, (long) storage.createUniqIdForMessage());
    }

	@Test
    public void addMessageWithUniqId() throws Exception {
        Long id = storage.addMessageWithUniqId(new Message("Новое сообщение"));
        assertEquals(16L, (long) id);
    }

    @Test
	public void addMessage() throws Exception {
		Message message = new Message("Новое сообщение");
		storage.addMessage(5L, message);

		Message newMessage = new Message("Ещё сообщение");
		exception.expect(MessageAlreadyExists.class);
		exception.expectMessage(String.format("Message with id '%d' already exists", 2L));
		storage.addMessage(2L, newMessage);
	}

    @Test
    public void deleteMessageById() throws Exception {
        storage.deleteMessageById(1L);

        exception.expect(MessageNotFound.class);
        exception.expectMessage("No message with such id: 3");
        storage.deleteMessageById(3L);
    }

}