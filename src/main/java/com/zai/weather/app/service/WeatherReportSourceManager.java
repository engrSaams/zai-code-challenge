package com.zai.weather.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zai.weather.app.model.WeatherReport;
import com.zai.weather.app.service.impl.OpenWeatherMapAPI;
import com.zai.weather.app.service.impl.WeatherStackAPI;

@Service
public class WeatherReportSourceManager {

	private WeatherStackAPI weatherStackAPI;
	private OpenWeatherMapAPI openWeatherMapAPI;
	private CacheService cacheService;
	
    @Autowired
    public WeatherReportSourceManager(WeatherStackAPI weatherStackAPI, OpenWeatherMapAPI openWeatherMapAPI, CacheService cacheService) {
        this.weatherStackAPI = weatherStackAPI;
        this.openWeatherMapAPI = openWeatherMapAPI;
        this.cacheService = cacheService;
    }
    
    @Cacheable(value = "weatherCache", key = "#root.method.name")
    public WeatherReport getWeatherReport(String city) {
    	WeatherReport weatherReport = new WeatherReport();
    	
    	//Weather Stack API
    	weatherReport = weatherStackAPI.getWeatherReport(city);
    	if (weatherReport.hasValidData()) return weatherReport; 
    	
    	//Open Weather API - failover
    	weatherReport = openWeatherMapAPI.getWeatherReport(city);
    	if(weatherReport.hasValidData()) return weatherReport;

    	//Add future providers here...
    	
    	//Return previous cache as stale response if all providers are down
    	return cacheService.restoreCache();
    	
    }
	
}
