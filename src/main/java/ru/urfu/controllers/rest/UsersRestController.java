package ru.urfu.controllers.rest;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.urfu.controllers.rest.models.ApiError;
import ru.urfu.entities.User;
import ru.urfu.storage.users.UsersStorage;
import ru.urfu.storage.users.exceptions.UserAlreadyExists;
import ru.urfu.storage.users.exceptions.UserNotFound;
import ru.urfu.storage.users.exceptions.UsersStorageException;

import javax.inject.Inject;
import javax.inject.Named;

@RestController
@RequestMapping("/rest/users")
public class UsersRestController {
	private final Log logger = LogFactory.getLog(getClass());

	@Inject
	@Named("usersStorage")
	private UsersStorage usersStorage;

	@GetMapping("get")
	ResponseEntity<User> getUser(@RequestParam("login") String login) throws UserNotFound {
		return ResponseEntity.ok(usersStorage.getByLogin(login));
	}

	@PostMapping("add")
	ResponseEntity<User> addUser(@RequestBody User user) throws UserAlreadyExists {
		return ResponseEntity.ok(usersStorage.add(user));
	}

	@ExceptionHandler(UsersStorageException.class)
	public ResponseEntity<ApiError> handleMessageNotFound(UsersStorageException ex) {
		ApiError bodyOfResponse = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
		logger.warn(ex.getLocalizedMessage());
		return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
	}
}
