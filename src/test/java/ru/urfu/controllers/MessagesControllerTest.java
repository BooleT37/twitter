package ru.urfu.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.urfu.models.Message;
import ru.urfu.storageManager.StorageManager;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
public class MessagesControllerTest {
	@Mock
	private StorageManager storageManager;

	@InjectMocks
	private MessagesRestController messagesRestController;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(messagesRestController).build();

		Map<Long, Message> messages = new HashMap<Long, Message>() {};
		messages.put(1L, new Message("Первое тест сообщение"));
		messages.put(2L, new Message("Второе тест сообщение"));
		when(storageManager.getAllMessages()).thenReturn(messages);
	}

	@Test
	public void messages() throws Exception {
		mockMvc.perform(get("/messages"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html"));
	}



}