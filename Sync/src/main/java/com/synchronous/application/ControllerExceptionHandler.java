package com.synchronous.application;

import java.util.concurrent.CompletionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Fault> generalExceptionHandler(Exception exception) {
		System.out.println("Exception handled:" + exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Fault(exception.getMessage(), "400"));
	}
	
	@ExceptionHandler(value = CompletionException.class)
	public ResponseEntity<Fault> generalExceptionHandler(CompletionException exception) {
		System.out.println("Exception handled:" + exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Fault(exception.getMessage(), "400"));
	}
	
}
