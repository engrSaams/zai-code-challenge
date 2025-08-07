package com.zai.weather.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zai.weather.app.model.WeatherReport;
import com.zai.weather.app.service.impl.OpenWeatherMapAPI;
import com.zai.weather.app.service.impl.WeatherStackAPI;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class WeatherReportSourceManagerTest {

	@InjectMocks
	private WeatherReportSourceManager weatherReportSourceManager;
	
	@Mock
	private CacheService cacheService;
	
	@Mock
	private WeatherStackAPI weatherStackAPI;
	
	@Mock
	private OpenWeatherMapAPI openWeatherMapAPI;
	
	private WeatherReport weatherReport_nullFields;
	private WeatherReport weatherReport_weatherStackAPI;
	private WeatherReport weatherReport_OpenWeatherMapAPI;
	private WeatherReport weatherReport_cache;
	
	@BeforeAll
	void setup() {
		weatherReport_nullFields = new WeatherReport(null, null);
		weatherReport_weatherStackAPI = new WeatherReport("1.1", "2.2");
		weatherReport_OpenWeatherMapAPI = new WeatherReport("3.3", "4.4");
		weatherReport_cache = new WeatherReport("6.6", "1.9");
		
	}
	
	@Test
	void getWeatherReport_shouldReturnStaleCacheWeatherReport() {
		
		when(weatherStackAPI.getWeatherReport(anyString())).thenReturn(weatherReport_nullFields);
		when(openWeatherMapAPI.getWeatherReport(anyString())).thenReturn(weatherReport_nullFields);
		when(cacheService.restoreCache()).thenReturn(weatherReport_cache);
		
		WeatherReport weatherReport = weatherReportSourceManager.getWeatherReport("Test City");
		assertEquals(weatherReport.getTemperatureDegrees(), "1.9");
		assertEquals(weatherReport.getWindSpeed(), "6.6");
	}
	
	@Test
	void getWeatherReport_shouldReturnWeatherStackAPIWeatherReport() {
		
		when(weatherStackAPI.getWeatherReport(anyString())).thenReturn(weatherReport_weatherStackAPI);
		
		WeatherReport weatherReport = weatherReportSourceManager.getWeatherReport("Test City");
		assertEquals(weatherReport.getTemperatureDegrees(), "2.2");
		assertEquals(weatherReport.getWindSpeed(), "1.1");
		
	}
	
	@Test
	void getWeatherReport_shouldReturnOpenWeatherMapAPIWeatherReport() {
		
		when(weatherStackAPI.getWeatherReport(anyString())).thenReturn(weatherReport_nullFields);
		when(openWeatherMapAPI.getWeatherReport(anyString())).thenReturn(weatherReport_OpenWeatherMapAPI);
		
		WeatherReport weatherReport = weatherReportSourceManager.getWeatherReport("Test City");
		assertEquals(weatherReport.getTemperatureDegrees(), "4.4");
		assertEquals(weatherReport.getWindSpeed(), "3.3");
		
	}

}
