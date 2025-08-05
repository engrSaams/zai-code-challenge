package com.zai.weather.app.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.zai.weather.app.service.impl.WeatherStackAPI;

@SpringBootTest
class WeatherStackServiceTest {

	@Autowired
	@InjectMocks
	private WeatherStackAPI weatherStackAPI;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Value("${weather.stack.url}")
	private String weatherStackUrl;
	
	@Test
	void testGetWeatherReport() {
		
		assertAll( () -> {
			ResponseEntity<String> responseEntity = new ResponseEntity<>("Test", null, HttpStatus.OK);
			when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
			weatherStackAPI.getWeatherReport();
		});
		
	}
	
	@Test
	void testGetWeatherReportWithException() {
		ReflectionTestUtils.setField(weatherStackAPI, "weatherStackUrl", "dummy url");
		assertAll( () -> {
			weatherStackAPI.getWeatherReport();
		});
	}

}
