package ru.urfu.storage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaMessagesStorageIT {

	@Inject @Named("jpaMessagesStorage")
	private MessagesStorage storage;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void addMessage() throws Exception {
		String content = "Тестовое сообщение для getMessageById";
		Message message = new Message(content);
		Message addedMessage = storage.addMessage(message);
		assertNotNull(addedMessage);
		assertNotNull(addedMessage.getId());
		assertEquals(addedMessage.getContent(), content);
	}

	@Test
	public void getAndDeleteMessageById() throws Exception {
		String content = "Тестовое сообщение для getAndDeleteMessageById";
		Message message = new Message(content);
		Message added = storage.addMessage(message);
		Long id = added.getId();

		Message found = storage.getMessageById(id);
		assertEquals(found.getId(), id);
		assertEquals(found.getContent(), content);

		Message deleted = storage.deleteMessageById(id);
		assertEquals(deleted.getId(), id);
		assertEquals(deleted.getContent(), content);

		exception.expect(MessageNotFound.class);
		exception.expectMessage(String.format("No message with such id: %s", id));
		storage.getMessageById(id);

		exception.expect(MessageNotFound.class);
		exception.expectMessage(String.format("No message with such id: %s", id));
		storage.deleteMessageById(id);
	}

	@Test
	public void getAllMessages() throws Exception {
		String content = "Тестовое сообщение для getAllMessages";
		Message message = new Message(content);
		Message added = storage.addMessage(message);

		List<Message> allMessages = storage.getAllMessages();
		Optional<Message> found = allMessages.stream().filter(msg -> msg.getId().equals(added.getId())).findFirst();
		assertTrue(found.isPresent());
		if (found.isPresent()) {
			assertEquals(content, found.get().getContent());
			assertEquals(added.getId(), found.get().getId());
		}

		storage.deleteMessageById(message.getId());
	}


	@Test
	public void isStorageEmpty() throws Exception {
		Message added = storage.addMessage(new Message("Тестовое сообщение для isStorageEmpty"));
		assertFalse(storage.isEmpty());
		storage.deleteMessageById(added.getId());
	}

}