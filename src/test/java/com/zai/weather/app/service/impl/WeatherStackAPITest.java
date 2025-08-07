package com.zai.weather.app.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.zai.weather.app.model.WeatherReport;
import com.zai.weather.app.util.JsonUtil;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class WeatherStackAPITest {

	String testUrl = "http://localhost:8080/v1/test";

	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private JsonUtil jsonUtil;
	
	@InjectMocks
	private WeatherStackAPI weatherStackAPI;
	
	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(weatherStackAPI, "weatherStackUrl", testUrl);
	}
	
    private static Stream<Arguments> provideExceptionTestCases() {
        return Stream.of(
            Arguments.of(RestClientException.class),
            Arguments.of(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR).getClass()),
            Arguments.of(new HttpClientErrorException(HttpStatus.NOT_FOUND).getClass())
        );
    }
	
    @ParameterizedTest
    @MethodSource("provideExceptionTestCases")
	void getWeatherReport_exception_ShouldReturnNullWeatherReport(Class<? extends Throwable> expectedException) {
		when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(expectedException);
		WeatherReport weatherReport = weatherStackAPI.getWeatherReport("Test City");
		verify(restTemplate).getForEntity(anyString(), eq(String.class));
		
		assertNull(weatherReport.getTemperatureDegrees());
		assertNull(weatherReport.getWindSpeed());
	}
	
	@Test
	void getWeatherReport_HappyPath_ShouldReturnValidWeatherReport() {	
		ResponseEntity<String> mockResponseEntity = new ResponseEntity<>("TestBody", HttpStatus.OK);
		when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(mockResponseEntity);

        JsonNode mockResponseJson = mock(JsonNode.class);
        JsonNode mockCurrentNode = mock(JsonNode.class);
        JsonNode mockWindSpeedNode = mock(JsonNode.class);
        JsonNode mockTempratureNode = mock(JsonNode.class);
		
        when(mockResponseJson.get("current")).thenReturn(mockCurrentNode);
        when(mockCurrentNode.get("wind_speed")).thenReturn(mockWindSpeedNode);
        when(mockCurrentNode.get("temperature")).thenReturn(mockTempratureNode);
        when(mockWindSpeedNode.toPrettyString()).thenReturn("10.5");
        when(mockTempratureNode.toPrettyString()).thenReturn("30.3");
        
		when(jsonUtil.convertToJsonNode(anyString())).thenReturn(mockResponseJson);

		WeatherReport weatherReport = weatherStackAPI.getWeatherReport("Test City");
		
		assertEquals(weatherReport.getWindSpeed(), "10.5");
		assertEquals(weatherReport.getTemperatureDegrees(), "30.3");
		
		verify(jsonUtil).convertToJsonNode(anyString());	
		verify(restTemplate).getForEntity(anyString(), eq(String.class));
	}
	
	@Test
	void buildUrl_shouldReturnValidUrl() {
		String url = weatherStackAPI.buildUrl("Test City");
		assertDoesNotThrow(() -> new URL(url));
	}

}
