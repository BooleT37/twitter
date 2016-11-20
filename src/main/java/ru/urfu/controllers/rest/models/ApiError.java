package ru.urfu.controllers.rest.models;

import org.springframework.http.HttpStatus;

public class ApiError {
	private HttpStatus error;
	private String message;

	public ApiError(HttpStatus error, String message) {
		this.error = error;
		this.message = message;
	}

	public HttpStatus getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}
}
