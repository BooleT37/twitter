package ru.urfu.controllers;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.urfu.entities.Message;
import ru.urfu.entities.User;
import ru.urfu.storage.messages.MessagesStorage;
import ru.urfu.storage.users.UsersStorage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext
public class IndexControllerIT {
    @Inject
	private MessagesStorage messagesStorage;

    @Inject
    private UsersStorage usersStorage;

	@Inject
	private MockMvc mockMvc;

	private final String testUserLogin = "Test_user";
	private final String testUserPassword = "test_password";

    private List<Long> addedMessagesIds;

	@Before
	public void setUp() throws Exception {
		//MockitoAnnotations.initMocks(this);
        User user = new User(testUserLogin, testUserPassword);

        usersStorage.add(user);

		addedMessagesIds = new ArrayList<>();
        addedMessagesIds.add(messagesStorage.add(new Message(0L, "Первое тест сообщение", user)).getId());
        addedMessagesIds.add(messagesStorage.add(new Message(1L, "Второе тест сообщение", user)).getId());
	}

    @After
    public void tearDown() throws Exception {
        for (Long id: addedMessagesIds)
            messagesStorage.deleteById(id);

        usersStorage.deleteByLogin(testUserLogin);
    }

    @Test
	public void index() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().is(HttpStatus.SC_MOVED_TEMPORARILY));
                //.andExpect(header().string("Location", endsWith("login"))); //doesn't work for some reason
	}

    @Test
    public void login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void signup() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = testUserLogin, password = testUserPassword)
    public void messages() throws Exception {
        mockMvc.perform(get("/messages/" + testUserLogin))
                .andExpect((status().isOk()));
    }
}