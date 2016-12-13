package ru.urfu.storage.users;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.urfu.models.User;
import ru.urfu.storage.users.exceptions.UserAlreadyExists;
import ru.urfu.storage.users.exceptions.UserNotFound;

import static org.junit.Assert.assertEquals;


public class TemporalUsersStorageTest {
	private final String login = "test_login";
	private final String password = "test_password";

	private TemporalUsersStorage storage = new TemporalUsersStorage();

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		storage.add(new User(login, password));
	}

	@Test
	public void add() throws Exception {
		String newLogin = "new_login";
		String newPassword = "new_password";
		User user = storage.add(new User(newLogin, newPassword));
		assertEquals(user.getLogin(), newLogin);
		assertEquals(user.getPassword(), newPassword);

		exception.expect(UserAlreadyExists.class);
		exception.expectMessage(String.format("User '%s' already exists", newLogin));
		storage.add(new User(newLogin, newPassword));
	}

	@Test
	public void getByLogin() throws Exception {
		User user = storage.getByLogin(login);
		assertEquals(user.getLogin(), login);
		assertEquals(user.getPassword(), password);

		String notExistentLogin = "not_existent_login";
		exception.expect(UserNotFound.class);
		exception.expectMessage(String.format("No user with name '%s'", notExistentLogin));
		storage.getByLogin(notExistentLogin);
	}


	@Test
	public void deleteByLogin() throws Exception {
		User user = storage.deleteByLogin(login);
		assertEquals(user.getLogin(), login);
		assertEquals(user.getPassword(), password);

		exception.expect(UserNotFound.class);
		exception.expectMessage(String.format("No user with name '%s'", login));
		storage.deleteByLogin(login);
	}

}