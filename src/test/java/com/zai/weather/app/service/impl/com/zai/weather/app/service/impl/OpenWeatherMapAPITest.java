package com.zai.weather.app.service.impl.com.zai.weather.app.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.zai.weather.app.model.WeatherReport;
import com.zai.weather.app.service.impl.OpenWeatherMapAPI;
import com.zai.weather.app.util.JsonUtil;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class OpenWeatherMapAPITest {

	String testUrl = "http://localhost:8080/v1/test";
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private JsonUtil jsonUtil;
	
	@InjectMocks
	private OpenWeatherMapAPI openWeatherMapAPI;
	
	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(openWeatherMapAPI, "openWeatherUrl", testUrl);
	}
	
	@Test
	void OpenWeatherMapAPI_getWeatherReport_ShouldReturnNullWeatherReport() {
		when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(RestClientException.class);
		WeatherReport weatherReport = openWeatherMapAPI.getWeatherReport("Test City");
		
		assertNull(weatherReport.getTemperatureDegrees());
		assertNull(weatherReport.getWindSpeed());
	}
	
	@Test
	void OpenWeatherMapAPI_getWeatherReport_ShouldReturnValidWeatherReport() {	
		ResponseEntity<String> mockResponseEntity = new ResponseEntity<>("TestBody", HttpStatus.OK);
		when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(mockResponseEntity);

        JsonNode mockResponseJson = mock(JsonNode.class);
        JsonNode mockWindNode = mock(JsonNode.class);
        JsonNode mockSpeedNode = mock(JsonNode.class);
        JsonNode mockMainNode = mock(JsonNode.class);
        JsonNode mockTempNode = mock(JsonNode.class);
        
        when(mockResponseJson.get("wind")).thenReturn(mockWindNode);
        when(mockWindNode.get("speed")).thenReturn(mockSpeedNode);
        when(mockResponseJson.get("main")).thenReturn(mockMainNode);
        when(mockMainNode.get("temp")).thenReturn(mockTempNode);
        when(mockSpeedNode.toPrettyString()).thenReturn("16.5");
        when(mockTempNode.toPrettyString()).thenReturn("31.5");
        
		when(jsonUtil.convertToJsonNode(anyString())).thenReturn(mockResponseJson);

		WeatherReport weatherReport = openWeatherMapAPI.getWeatherReport("Test City");
		
		assertEquals(weatherReport.getWindSpeed(), "16.5");
		assertEquals(weatherReport.getTemperatureDegrees(), "31.5");
		
		verify(jsonUtil).convertToJsonNode(anyString());	
		verify(restTemplate).getForEntity(anyString(), eq(String.class));
	}
	
	@Test
	void OpenWeatherMapAPI_buildUrl_shouldReturnValidUrl() {
		String url = openWeatherMapAPI.buildUrl("Test City");
		assertDoesNotThrow(() -> new URL(url));
	}

}
