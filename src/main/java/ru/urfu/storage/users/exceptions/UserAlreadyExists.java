package ru.urfu.storage.users.exceptions;

public class UserAlreadyExists extends UsersStorageException {
	public UserAlreadyExists(String message) {
		super(message);
	}
}
