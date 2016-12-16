package ru.urfu.storage.users;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.urfu.entities.User;
import ru.urfu.storage.users.exceptions.UserNotFound;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JpaUsersStorageTest {
	@Mock
	private EntityManager em;

	@InjectMocks
	private JpaUsersStorage storage = new JpaUsersStorage();

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getByLogin() throws Exception {
		String existingLogin = "test_login";
		String password = "test_password";

		when(em.find(User.class, existingLogin)).thenReturn(new User(existingLogin, password));
		assertEquals(password, storage.getByLogin(existingLogin).getPassword());

		String notExistingLogin = "not_existing_login";
		when(em.find(User.class, notExistingLogin)).thenReturn(null);
		exception.expect(UserNotFound.class);
		exception.expectMessage(String.format("No user with name '%s'", notExistingLogin));
		storage.getByLogin(notExistingLogin);
	}

	@Test
	public void add() throws Exception {
		storage.add(new User("test_login", "test_password"));
		verify(em).persist(any(User.class));
	}

	@Test
	public void deleteByLogin() throws Exception {
		String login = "test_login";
		String password = "test_password";
		when(em.find(User.class, login)).thenReturn(new User(login, password));
		User user = storage.deleteByLogin(login);
		assertEquals(password, user.getPassword());
		verify(em).remove((any(User.class)));
	}

}