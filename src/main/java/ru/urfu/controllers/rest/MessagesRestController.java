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
import ru.urfu.models.Message;
import ru.urfu.storage.messages.MessagesStorage;
import ru.urfu.storage.messages.exceptions.MessageNotFound;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;


@RestController
public class MessagesRestController {

	private final Log logger = LogFactory.getLog(getClass());

    @Inject @Named("messagesStorage")
    private MessagesStorage messagesStorage;

    @GetMapping("/messages/all")
	List<Message> getAllMessages() {
        return messagesStorage.getAll();
    }

    @GetMapping("/messages/get")
	ResponseEntity<Message> getMessage(@RequestParam("id") Long id) throws MessageNotFound {
		return ResponseEntity.ok(messagesStorage.getById(id));
    }

    @PostMapping("/messages/add")
	ResponseEntity addMessage(@RequestBody Message message) {
		messagesStorage.add(message);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/delete")
	ResponseEntity<ApiSuccess> deleteMessage(@RequestBody DeleteMessageRequestModel model) throws MessageNotFound {
    	long id = model.getId();
		messagesStorage.deleteById(id);
		return ResponseEntity.ok(new ApiSuccess(String.format("Successfully deleted a message with id %d", id)));
	}

	@ExceptionHandler(MessageNotFound.class)
	public ResponseEntity<ApiError> handleMessageNotFound(MessageNotFound ex) {
		ApiError bodyOfResponse = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
		logger.warn(ex.getLocalizedMessage());
		return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
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
