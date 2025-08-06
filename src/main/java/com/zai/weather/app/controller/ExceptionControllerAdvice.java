package com.zai.weather.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.zai.weather.app.model.ErrorResponse;

@RestControllerAdvice
public class ExceptionControllerAdvice {
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException httpClientErrorException) {
		ErrorResponse errorResponse = new ErrorResponse(httpClientErrorException.getMessage(), httpClientErrorException.getStatusText());
		return new ResponseEntity<>(errorResponse, httpClientErrorException.getStatusCode());
	}
	
}
