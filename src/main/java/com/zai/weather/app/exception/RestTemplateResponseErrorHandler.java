package com.zai.weather.app.exception;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		
        return response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError();
        
	}
	
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		
		if (response.getStatusCode().is5xxServerError()) {
			
			throw new HttpServerErrorException(response.getStatusCode());
			
		} else if (response.getStatusCode().is4xxClientError()) {
			
			throw new HttpClientErrorException(response.getStatusCode());
			
		} else {
			
            throw new RestClientException("Unknown error occurred with status code: " + response.getStatusCode() + ", Response: " + response.getBody());
			
		}
		
	}

}
