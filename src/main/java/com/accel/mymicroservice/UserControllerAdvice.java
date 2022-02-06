package com.accel.mymicroservice;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice 
public class UserControllerAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(UserControllerAdvice.class);

	
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	       String error = "Malformed JSON request";
	       System.ou45t.println(error);
	       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, error));
	   }

	   private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
	       return new ResponseEntity<>(apiError, apiError.getStatus());
	   }
	   
	   @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="IOException occured")
		@ExceptionHandler(IOException.class)
		public void handleIOException(){
			logger.error("IOException handler executed");
			//returning 404 error code
		}

	
}
