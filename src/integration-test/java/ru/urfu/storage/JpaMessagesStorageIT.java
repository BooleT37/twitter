package ru.urfu.storage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.urfu.models.Message;
import ru.urfu.storage.messages.exceptions.MessageNotFound;
import ru.urfu.storage.messages.MessagesStorage;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class JpaMessagesStorageIT {

	@Inject @Named("jpaMessagesStorage")
	private MessagesStorage storage;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void addMessage() throws Exception {
		String content = "Тестовое сообщение для getById";
		Message message = new Message(content);
		Message addedMessage = storage.add(message);
		assertNotNull(addedMessage);
		assertNotNull(addedMessage.getId());
		assertEquals(addedMessage.getContent(), content);

		storage.deleteById(addedMessage.getId());
	}

	@Test
	public void getAndDeleteMessageById() throws Exception {
		String content = "Тестовое сообщение для getAndDeleteMessageById";
		Message message = new Message(content);
		Message added = storage.add(message);
		Long id = added.getId();

		Message found = storage.getById(id);
		assertEquals(found.getId(), id);
		assertEquals(found.getContent(), content);

		Message deleted = storage.deleteById(id);
		assertEquals(deleted.getId(), id);
		assertEquals(deleted.getContent(), content);

		exception.expect(MessageNotFound.class);
		exception.expectMessage(String.format("No message with such id: %s", id));
		storage.getById(id);

		exception.expect(MessageNotFound.class);
		exception.expectMessage(String.format("No message with such id: %s", id));
		storage.deleteById(id);
	}

	@Test
	public void getAllMessages() throws Exception {
		String content = "Тестовое сообщение для getAll";
		Message message = new Message(content);
		Message added = storage.add(message);

		List<Message> allMessages = storage.getAll();
		Optional<Message> found = allMessages.stream().filter(msg -> msg.getId().equals(added.getId())).findFirst();
		assertTrue(found.isPresent());
		if (found.isPresent()) {
			assertEquals(content, found.get().getContent());
			assertEquals(added.getId(), found.get().getId());
		}

		storage.deleteById(message.getId());
	}


	@Test
	public void isStorageEmpty() throws Exception {
		Message added = storage.add(new Message("Тестовое сообщение для isStorageEmpty"));
		assertFalse(storage.isEmpty());
		storage.deleteById(added.getId());
	}

}