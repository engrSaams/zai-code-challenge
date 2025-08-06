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
	
	@Autowired
	private CacheService cacheService;
	
    @Autowired
    public WeatherReportSourceManager(WeatherStackAPI weatherStackAPI, OpenWeatherMapAPI openWeatherMapAPI) {
        this.weatherStackAPI = weatherStackAPI;
        this.openWeatherMapAPI = openWeatherMapAPI;
    }
    
//    @Cacheable(value = "weatherCache", key = "#root.method.name", unless = "#WeatherReport?.windSpeed == null || #WeatherReport?.temperatureDegrees == null")
    @Cacheable(value = "weatherCache", key = "#root.method.name")
    public WeatherReport getWeatherReport(String city) {
    	WeatherReport weatherReport = new WeatherReport();
    	
    	//Weather Stack API
    	weatherReport = weatherStackAPI.getWeatherReport(city);
    	if (weatherReport.hasValidData()) return weatherReport; 
    	
    	//Open Weather API - failover
    	weatherReport = openWeatherMapAPI.getWeatherReport(city);
    	if(weatherReport.hasValidData()) return weatherReport;

    	//Add future APIs here...
    	
    	//Return precious cache as stale response if all providers are down
    	return cacheService.restoreCache();
    	
    }
	
}
