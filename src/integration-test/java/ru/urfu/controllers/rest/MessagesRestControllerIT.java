package ru.urfu.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.urfu.models.Message;
import ru.urfu.storage.MessagesStorage;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MessagesRestControllerIT {
	private static final String getAllMessagesPath = "/getAll";
	private static final String getMessagePath = "/getMessage";

	@Inject @Named("messagesStorage")
	private MessagesStorage storage;

	private List<String> testMessageContents;

	private List<Message> testMessages;

	@Before
	public void setUp() {
		testMessageContents = new ArrayList<>();
		testMessageContents.add("Тестовое сообщение 1");
		testMessageContents.add("Тестовое сообщение 2");
		testMessageContents.add("Тестовое сообщение 3");

		testMessages = new ArrayList<>();

		for (String content: testMessageContents) {
			testMessages.add(storage.add(new Message(content)));
		}
	}

	@Test
	public void getAllMessages() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		List<Map<String, Object>> messageObjects = get(getAllMessagesPath).as(List.class);
		List<Message> messages = messageObjects.stream().map(message -> mapper.convertValue(message, Message.class)).collect(Collectors.toList());

		assertThat(messages, hasItems(
				allOf(
						hasContent(testMessageContents.get(0)),
						hasId(testMessages.get(0).getId())
				),
				allOf(
						hasContent(testMessageContents.get(1)),
						hasId(testMessages.get(1).getId())
				),
				allOf(
						hasContent(testMessageContents.get(2)),
						hasId(testMessages.get(2).getId())
				)
		));
	}



	@Test
	public void getMessage() throws Exception {
		Long id = testMessages.get(0).getId();
		given()
				.param("id", id)
		.when()
				.get(getMessagePath)
				.then()
				.statusCode(HttpStatus.SC_OK)
				.body("content", equalTo(testMessageContents.get(0)));

	}



	@Test
	public void addMessage() throws Exception {

	}

	@Test
	public void deleteMessage() throws Exception {

	}

	@After
	public void tearDown() throws MessageNotFound {
		for (Message message: testMessages) {
			storage.deleteById(message.getId());
		}
	}

	private Matcher<Message> hasContent(final String str) {
		return new BaseMatcher<Message>() {
			@Override
			public boolean matches(final Object item) {
				final Message foo = (Message) item;
				return str.equals(foo.getContent());
			}

			@Override
			public void describeTo(final Description description) {
				description.appendText("content = ").appendValue(str);
			}
		};
	}

	private Matcher<Message> hasId(final Long id) {
		return new BaseMatcher<Message>() {
			@Override
			public boolean matches(final Object item) {
				final Message foo = (Message) item;
				return id.equals(foo.getId());
			}

			@Override
			public void describeTo(final Description description) {
				description.appendText("id = ").appendValue(id);
			}
		};
	}
}