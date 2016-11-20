package ru.urfu.storage.exceptions;

public class MessageAlreadyExists extends StorageManagerException {
	private MessageAlreadyExists(String message) {
		super(message);
	}

	public MessageAlreadyExists(long id) {
		this(String.format("Message with id '%d' already exists", id));
	}

	public MessageAlreadyExists() {
		this("Message with this id already exists");
	}
}
