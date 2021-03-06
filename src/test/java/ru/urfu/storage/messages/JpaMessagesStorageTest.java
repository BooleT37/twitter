package ru.urfu.storage.messages;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.urfu.entities.Message;
import ru.urfu.entities.User;
import ru.urfu.storage.messages.exceptions.MessageNotFound;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JpaMessagesStorageTest {
	private final User testUser = new User("Test_user", "test_password");

	private final String[] contents = new String[] {
			"Первое тест сообщение",
			"Второе тест сообщение",
			"Третье тест сообщение",
			"Четвертое тест сообщение"
	};

	@Mock
	private EntityManager em;

	@Mock
	private TypedQuery<Message> messageQueryResult;
	@Mock
	private TypedQuery<Long> longQueryResult;

	@InjectMocks
	private JpaMessagesStorage storage = new JpaMessagesStorage();

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getMessageById() throws Exception {
		long existingId = 0L;
		when(em.find(Message.class, existingId)).thenReturn(new Message(existingId, contents[0], testUser));
		assertEquals(contents[0], storage.getById(existingId).getContent());

		long notExistingId = 5L;
		when(em.find(Message.class, notExistingId)).thenReturn(null);
		exception.expect(MessageNotFound.class);
		exception.expectMessage(String.format("No message with such id: %d", notExistingId));
		storage.getById(notExistingId);
	}

	@Test
	public void getAllMessages() throws Exception {
		List<Message> messages = new ArrayList<>();
		messages.add(new Message(0L, "Первое тест сообщение", testUser));
		messages.add(new Message(1L, "Второе тест сообщение", testUser));
		messages.add(new Message(2L, "Третье тест сообщение", testUser));
		messages.add(new Message(3L, "Четвертое тест сообщение", testUser));
		when(messageQueryResult.getResultList()).thenReturn(messages);
		when(em.createQuery("from " + Message.class.getName(), Message.class)).thenReturn(messageQueryResult);

		assertArrayEquals(contents, storage.getAll().stream().map(Message::getContent).toArray());
	}

	@Test
	public void addMessage() throws Exception {
		storage.add(new Message("Новое сообщение", testUser));
		verify(em).persist(any(Message.class));
	}

	@Test
	public void deleteMessageById() throws Exception {
		Long id = 1L;
		when(em.find(Message.class, id)).thenReturn(new Message(id, "Тестовое сообщение для удаления", testUser));
		Message message = storage.deleteById(id);
		assertEquals(id, message.getId());
		verify(em).remove(any(Message.class));
	}

	@Test
	public void isStorageEmpty() throws Exception {
		String queryString = "select count(*) from " + Message.class.getName();
		when(em.createQuery(queryString, Long.class)).thenReturn(longQueryResult);
		storage.isEmpty();
		verify(em).createQuery(queryString, Long.class);
	}

}