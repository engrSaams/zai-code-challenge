package com.zai.weather.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.zai.weather.app.model.WeatherReport;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

	@InjectMocks
	private CacheService cacheService;
	
    @BeforeEach
    void setup() {
        CacheService.weatherReport.setTemperatureDegrees(null);
        CacheService.weatherReport.setWindSpeed(null);
    }
	
	@Test
	void onRemoval_shouldBeAbleToStoreTheCacheAsStaleData() {
		WeatherReport weatherReport = new WeatherReport("1.1", "2.2");
		cacheService.onRemoval("Test Key", weatherReport, RemovalCause.EXPIRED);
		
		assertEquals(CacheService.weatherReport.getTemperatureDegrees(), "2.2");
		assertEquals(CacheService.weatherReport.getWindSpeed(), "1.1");
	}
	
	@Test
	void onRemoval_shouldNotStoreTheCache() {
		WeatherReport weatherReport = new WeatherReport(null, "2.2");
		cacheService.onRemoval("Test Key", weatherReport, RemovalCause.EXPIRED);
		
		assertEquals(CacheService.weatherReport.getTemperatureDegrees(), null);
		assertEquals(CacheService.weatherReport.getWindSpeed(), null);
	}
	
	@Test
	void restoreCache() {
		WeatherReport weatherReport = new WeatherReport("1.1", "2.2");
		cacheService.onRemoval("Test Key", weatherReport, RemovalCause.EXPIRED);
		
		WeatherReport weatherReport_fromCache = cacheService.restoreCache();
		assertEquals(CacheService.weatherReport.getTemperatureDegrees(), "2.2");
		assertEquals(CacheService.weatherReport.getWindSpeed(), "1.1");
	}

}
