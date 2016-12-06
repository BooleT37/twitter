package ru.urfu.storage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class JpaMessagesStorageTest {
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
		when(em.find(Message.class, existingId)).thenReturn(new Message(existingId, "Первое тест сообщение"));
		assertEquals(contents[0], storage.getMessageById(existingId).getContent());

		long notExistingId = 5L;
		when(em.find(Message.class, notExistingId)).thenReturn(null);
		exception.expect(MessageNotFound.class);
		exception.expectMessage(String.format("No message with such id: %d", notExistingId));
		storage.getMessageById(notExistingId);
	}

	@Test
	public void getAllMessages() throws Exception {
		List<Message> messages = new ArrayList<>();
		messages.add(new Message(0L, "Первое тест сообщение"));
		messages.add(new Message(1L, "Второе тест сообщение"));
		messages.add(new Message(2L, "Третье тест сообщение"));
		messages.add(new Message(3L, "Четвертое тест сообщение"));
		when(messageQueryResult.getResultList()).thenReturn(messages);
		when(em.createQuery("from " + Message.class.getName(), Message.class)).thenReturn(messageQueryResult);

		assertArrayEquals(contents, storage.getAllMessages().stream().map(Message::getContent).toArray());
	}

	@Test
	public void addMessage() throws Exception {
		storage.addMessage(new Message("Новое сообщение"));
		verify(em).persist(any(Message.class));
	}

	@Test
	public void deleteMessageById() throws Exception {
		Long id = 1L;
		when(em.find(Message.class, id)).thenReturn(new Message(id, "Тестовое сообщение для удаления"));
		Message message = storage.deleteMessageById(id);
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