package ru.urfu.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.urfu.entities.Message;
import ru.urfu.entities.User;
import ru.urfu.models.MessageJson;
import ru.urfu.storage.messages.MessagesStorage;
import ru.urfu.storage.messages.exceptions.MessageNotFound;
import ru.urfu.storage.users.UsersStorage;
import ru.urfu.storage.users.exceptions.UserAlreadyExists;
import ru.urfu.storage.users.exceptions.UserNotFound;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class MessagesRestControllerIT {
	private static final String getAllMessagesPath = "/rest/messages/all";
	private static final String getMessagePath = "/rest/messages/get";
	private static final String addMessagePath = "/rest/messages/add";
	private static final String deleteMessagePath = "/rest/messages/delete";

	@Inject @Named("messagesStorage")
	private MessagesStorage messagesStorage;

	@Inject @Named("usersStorage")
	private UsersStorage usersStorage;

	private User testUser;

	private List<String> testMessageContents;

	private List<Message> testMessages;

	@Before
	public void setUp() throws UserAlreadyExists {
        testUser = new User("test_user", "test_password");

        usersStorage.add(testUser);

		testMessageContents = new ArrayList<>();
		testMessageContents.add("Тестовое сообщение 1");
		testMessageContents.add("Тестовое сообщение 2");
		testMessageContents.add("Тестовое сообщение 3");

		testMessages = new ArrayList<>();

		for (String content: testMessageContents) {
			testMessages.add(messagesStorage.add(new Message(content, testUser)));
		}
	}

	@Test
	public void getAllMessages() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

        Response response = get(getAllMessagesPath);
        assertThat(response.getHeader("Content-Type"), startsWith("application/json"));
        List<Map<String, Object>> messageObjects = response.as(List.class);
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
	public void addAndDeleteMessage() throws Exception {
	    final MessageJson testMessage = new MessageJson("Test content", testUser.getLogin());

	    //Add new message
        Response response = given()
                .contentType("application/json")
                .body(testMessage)
                .when()
                .post(addMessagePath);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("content", equalTo(testMessage.getContent()));

        Message addedMessage = response.as(Message.class);

        //Delete added message
        given()
                .contentType("application/json")
                .body(addedMessage)
                .when()
                .delete(deleteMessagePath)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", not(isEmptyOrNullString()));

        Response deleteResponse = given()
                .contentType("application/json")
                .body(addedMessage)
                .when()
                .delete(deleteMessagePath);
        deleteResponse
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("No message with such id: " + addedMessage.getId()));
	}

	@After
	public void tearDown() throws MessageNotFound, UserNotFound {
		for (Message message: testMessages) {
			messagesStorage.deleteById(message.getId());
		}

        usersStorage.deleteByLogin(testUser.getLogin());
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