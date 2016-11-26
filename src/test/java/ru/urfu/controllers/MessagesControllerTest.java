package ru.urfu.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.urfu.controllers.rest.MessagesRestController;
import ru.urfu.models.Message;
import ru.urfu.storage.Storage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessagesControllerTest {
	@Mock
	private Storage storage;

	@InjectMocks
	private MessagesRestController messagesRestController;

	@Inject
	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		List<Message> messages = new ArrayList<Message>() {};
		messages.add(new Message(0L, "Первое тест сообщение"));
		messages.add(new Message(1L, "Второе тест сообщение"));
		when(storage.getAllMessages()).thenReturn(messages);
	}

	@Test
	public void messages() throws Exception {
		mockMvc.perform(get("/messages"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html; charset=utf-8"));
	}



}