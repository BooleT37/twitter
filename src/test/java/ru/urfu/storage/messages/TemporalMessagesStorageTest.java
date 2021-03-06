package ru.urfu.storage.messages;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.urfu.entities.Message;
import ru.urfu.entities.User;
import ru.urfu.storage.messages.exceptions.MessageNotFound;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import static org.junit.Assert.*;

public class TemporalMessagesStorageTest {
	private final User testUser = new User("Test_user", "test_password");

	private TemporalMessagesStorage storage = new TemporalMessagesStorage();

	private Queue<Message> testMessages = new LinkedList<>();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

	@Test
	public void addMessage() throws Exception {
    	final String content = "Тестовое сообщение для add";
		Message testMessage = this.addTestMessage(new Message(content, testUser));
		assertNotNull(testMessage.getId());
		assertEquals(content, testMessage.getContent());
	}

    @Test
    public void getMessageById() throws Exception {
		final String content = "Тестовое сообщение для getById";
		Message testMessage = this.addTestMessage(new Message(content, testUser));
		Message message = storage.getById(testMessage.getId());
        assertEquals(content, message.getContent());
        assertEquals(testMessage.getId(), message.getId());

		//In general, for "Storage" interface, id of the message we've just added may be already taken,
		//but this is not the case for this implementation
        Long wrongId = testMessage.getId() + 1;
        exception.expect(MessageNotFound.class);
        exception.expectMessage(String.format("No message with such id: %s", wrongId));
        storage.getById(wrongId);
    }

    @Test
    public void getAllMessages() throws Exception {
		final String content = "Тестовое сообщение для getAll";
		Message addedMessage = this.addTestMessage(new Message(content, testUser));
		List<Message> allMessages = storage.getAll();
		Optional<Message> foundMessage = allMessages.stream().filter(message -> message.getId().equals(addedMessage.getId())).findFirst();

		assertTrue(foundMessage.isPresent());
		if (foundMessage.isPresent()) {
			assertEquals(foundMessage.get().getContent(),content);
			assertEquals(foundMessage.get().getId(), addedMessage.getId());
		}
    }

    @Test
    public void deleteMessageById() throws Exception {
		final String content = "Тестовое сообщение для deleteById";
		Message addedMessage = storage.add(new Message(content, testUser));
        Message deletedMessage = storage.deleteById(addedMessage.getId());
        assertEquals(deletedMessage.getContent(), content);
        assertEquals(deletedMessage.getId(), addedMessage.getId());

        //Trying to delete message once again, expecting error
        exception.expect(MessageNotFound.class);
        exception.expectMessage(String.format("No message with such id: %s", addedMessage.getId()));
        storage.deleteById(addedMessage.getId());
    }

    private Message addTestMessage(Message message) {
    	Message newMessage = storage.add(message);
		testMessages.add(newMessage);
		return newMessage;
	}

	@After
	public void deleteTestMessages() throws MessageNotFound {
		while (!testMessages.isEmpty()) {
			Message message = testMessages.remove();
			storage.deleteById(message.getId());
		}
	}

}