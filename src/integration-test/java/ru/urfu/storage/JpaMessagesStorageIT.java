package ru.urfu.storage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JpaMessagesStorageIT {

	@Inject @Named("jpaMessagesStorage")
	private MessagesStorage storage;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	/*@Test
	public void getMessageById() throws Exception {
	}*/

	@Test
	public void getAllMessages() throws Exception {
		storage.getAllMessages();
	}

	/*@Test
	public void addMessage() throws Exception {

	}

	@Test
	public void deleteMessageById() throws Exception {

	}

	@Test
	public void isStorageEmpty() throws Exception {

	}*/

}