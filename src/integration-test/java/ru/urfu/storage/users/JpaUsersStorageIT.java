package ru.urfu.storage.users;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.urfu.entities.User;
import ru.urfu.storage.users.exceptions.UserAlreadyExists;
import ru.urfu.storage.users.exceptions.UserNotFound;

import javax.inject.Inject;
import javax.inject.Named;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class JpaUsersStorageIT {
    @Inject
    @Named("jpaUsersStorage")
    private JpaUsersStorage storage;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final String testUserLogin = "Test_user";
    private final String testUserPassword = "test_password";

    @Test
    public void addAndDelete() throws Exception {
        //Adding user
        User user = new User(testUserLogin, testUserPassword);
        User addedUser = storage.add(user);

        assertNotNull(addedUser);
        assertEquals(addedUser.getLogin(), user.getLogin());
        assertEquals(addedUser.getPassword(), user.getPassword());

        //Adding user one more time, expecting exception
        boolean isExceptionThrown = false;
        try {
            storage.add(user);
        } catch (UserAlreadyExists e) {
            isExceptionThrown = true;
            assertEquals(e.getMessage(), String.format("User '%s' already exists", user.getLogin()));
        }
        assertTrue(isExceptionThrown);

        //Deleting user
        User deletedUser = storage.deleteByLogin(user.getLogin());
        assertNotNull(deletedUser);
        assertEquals(deletedUser.getLogin(), user.getLogin());
        assertEquals(deletedUser.getPassword(), user.getPassword());

        //Trying to delete already deleted user, expecting exception
        exception.expect(UserNotFound.class);
        exception.expectMessage(String.format("No user with name '%s'", user.getLogin()));
        storage.deleteByLogin(user.getLogin());
    }

    @Test
    public void get() throws Exception {
        User user = new User(testUserLogin, testUserPassword);
        storage.add(user);

        //Getting user
        User gotUser = storage.getByLogin(user.getLogin());
        assertNotNull(gotUser);
        assertEquals(gotUser.getLogin(), user.getLogin());
        assertEquals(gotUser.getPassword(), user.getPassword());

        storage.deleteByLogin(user.getLogin());

        //Trying to get deleted user, expecting exception
        exception.expect(UserNotFound.class);
        exception.expectMessage(String.format("No user with name '%s'", user.getLogin()));
        storage.getByLogin(user.getLogin());
    }

    @Test
    public void loadUserByUsername() throws Exception {
        User user = new User(testUserLogin, testUserPassword);
        storage.add(user);

        //checking existing user credentials
        UserDetails userDetails = storage.loadUserByUsername(user.getLogin());
        assertNotNull(userDetails);
        assertEquals(userDetails.getUsername(), user.getLogin());
        assertEquals(userDetails.getPassword(), user.getPassword()); //todo check for authorities

        storage.deleteByLogin(user.getLogin());

        //checking not existing user credentials
        exception.expect(UsernameNotFoundException.class);
        exception.expectMessage(String.format("No user with name '%s'", user.getLogin()));
        storage.loadUserByUsername(user.getLogin());
    }

}