package com.zai.weather.app.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.zai.weather.app.model.WeatherReport;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CacheService implements RemovalListener<Object, Object> {

	public static WeatherReport weatherReport = new WeatherReport();
	
	@Override
	public void onRemoval(@Nullable Object key, @Nullable Object value, RemovalCause cause) {

		log.info("Removed from Cache | Key: {} | Value: {} | Cause: {}", key, value, cause);
		
		WeatherReport weatherReport = (WeatherReport) value;
		if(weatherReport.hasValidData()) {
			CacheService.weatherReport = weatherReport;
			log.info("Cache Stored as Stale Data: " + CacheService.weatherReport);
		}

	}
	
	public WeatherReport restoreCache() {
		log.info("Restoring Stale Data: " + CacheService.weatherReport.toString());
		return CacheService.weatherReport;
	}
	
}
