package com.accel.mymicroservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

public class ApiError {
	private HttpStatus status;
	private String message;
	private List<String> errors;
	
	public ApiError(HttpStatus status, String message, HttpMessageNotReadableException ex) {
		super();
		this.setStatus(status);
		this.message = message;
		this.errors = Arrays.asList(ex.getMessage());
	}

	public ApiError(HttpStatus badRequest, String message, String localizedMessage) {
		this.setStatus(status);
		this.message = message;
		this.errors = Arrays.asList(localizedMessage);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
