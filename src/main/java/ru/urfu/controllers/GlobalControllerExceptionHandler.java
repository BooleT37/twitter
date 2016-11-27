package ru.urfu.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
class GlobalControllerExceptionHandler {
	private static final String DEFAULT_ERROR_VIEW = "error";

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAnyException(Exception e) {
		return new ModelAndView(DEFAULT_ERROR_VIEW, "errorMessage", e.getLocalizedMessage());
	}
}
