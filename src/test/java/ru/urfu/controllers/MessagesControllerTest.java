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
import java.util.TreeMap;

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

		TreeMap<Long, Message> messages = new TreeMap<Long, Message>() {};
		messages.put(1L, new Message("Первое тест сообщение"));
		messages.put(2L, new Message("Второе тест сообщение"));
		when(storage.getAllMessages()).thenReturn(messages);
	}

	@Test
	public void messages() throws Exception {
		mockMvc.perform(get("/messages"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html; charset=utf-8"));
	}



}