package ru.urfu.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.urfu.models.ApiError;
import ru.urfu.models.ApiSuccess;
import ru.urfu.models.Message;
import ru.urfu.storage.Storage;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aarkaev
 * @since 08.08.2016
 */
@RestController
public class MessagesRestController {

	private final Log logger = LogFactory.getLog(getClass());

    @Inject
    private Storage storage;

    @GetMapping("/getAllMessages")
	Map<Long, Message> getAllMessages() {
        return storage.getAllMessages();
    }

    @GetMapping("/getMessage")
	ResponseEntity<Message> getMessage(@RequestParam("id") Long id) throws MessageNotFound {
		return ResponseEntity.ok(storage.getMessageById(id));
    }

    @PostMapping("/addMessage")
	ResponseEntity addMessage(@RequestBody Message message) {
        long id = storage.addMessageWithUniqId(message);
        HashMap<String, Object> body = new HashMap<>();
        body.put("id", id);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/deleteMessage")
	ResponseEntity<ApiSuccess> deleteMessage(@RequestBody DeleteMessageRequestModel model) throws MessageNotFound {
    	long id = model.getId();
		storage.deleteMessageById(id);
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
