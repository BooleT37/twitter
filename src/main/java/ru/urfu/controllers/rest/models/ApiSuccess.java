package ru.urfu.controllers.rest.models;

public class ApiSuccess {
	private final String message;

	public ApiSuccess(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
