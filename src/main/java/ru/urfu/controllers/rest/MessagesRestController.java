package ru.urfu.controllers.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.urfu.controllers.rest.models.ApiError;
import ru.urfu.controllers.rest.models.ApiSuccess;
import ru.urfu.entities.Message;
import ru.urfu.entities.User;
import ru.urfu.models.MessageJson;
import ru.urfu.storage.messages.MessagesStorage;
import ru.urfu.storage.messages.exceptions.MessageNotFound;
import ru.urfu.storage.users.UsersStorage;
import ru.urfu.storage.users.exceptions.UserNotFound;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;


@RestController
@RequestMapping("/rest/messages") //todo return all errors as JSON
public class MessagesRestController {

	private final Log logger = LogFactory.getLog(getClass());

    @Inject @Named("messagesStorage")
    private MessagesStorage messagesStorage;

    @Inject @Named("usersStorage")
    private UsersStorage usersStorage;

    @GetMapping("all")
	List<Message> getAllMessages() {
        return messagesStorage.getAll();
    }

    @GetMapping("get")
	ResponseEntity<Message> getMessage(@RequestParam("id") Long id) throws MessageNotFound {
		return ResponseEntity.ok(messagesStorage.getById(id));
    }

    @PostMapping("add")
	ResponseEntity addMessage(@RequestBody MessageJson messageJson) throws UserNotFound {
    	User user = usersStorage.getByLogin(messageJson.getUserLogin());
    	Message message = new Message(messageJson.getId(), messageJson.getContent(), user);
		messagesStorage.add(message);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("delete")
	ResponseEntity<ApiSuccess> deleteMessage(@RequestBody DeleteMessageRequestModel model) throws MessageNotFound {
    	long id = model.getId();
		messagesStorage.deleteById(id);
		return ResponseEntity.ok(new ApiSuccess(String.format("Successfully deleted a message with id %d", id)));
	}

	@ExceptionHandler({MessageNotFound.class, UserNotFound.class})
	public ResponseEntity<ApiError> handleExceptions(Exception ex) {
		ApiError bodyOfResponse = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
		logger.warn(ex.getLocalizedMessage());
		return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
	}

	public static class DeleteMessageRequestModel {
    	private long id;
		@JsonCreator
		public DeleteMessageRequestModel(@JsonProperty("id")long id) {
			this.id = id;
		}
		long getId() {
			return id;
		}
	}
}
