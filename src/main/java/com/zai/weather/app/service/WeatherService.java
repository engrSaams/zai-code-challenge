package com.zai.weather.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zai.weather.app.model.WeatherReport;
import com.zai.weather.app.service.impl.OpenWeatherMapAPI;
import com.zai.weather.app.service.impl.WeatherStackAPI;

@Service
public class WeatherService {

	private WeatherStackAPI weatherStackAPI;
	private OpenWeatherMapAPI openWeatherMapAPI;
	
    @Autowired
    public WeatherService(WeatherStackAPI weatherStackAPI, OpenWeatherMapAPI openWeatherMapAPI) {
        this.weatherStackAPI = weatherStackAPI;
        this.openWeatherMapAPI = openWeatherMapAPI;
    }
    
    @Cacheable(value = "weatherCache", unless = "#result == null || #result.windSpeed == null || #result.temperatureDegrees == null")
    public WeatherReport getWeatherReport() {
    	WeatherReport weatherReport = weatherStackAPI.perform();
    	//Failover
    	if(hasNull(weatherReport)) {
    		weatherReport = openWeatherMapAPI.perform();
    	}
    	return weatherReport;
    }
    
    private boolean hasNull(WeatherReport weatherReport) {	
    	if (weatherReport.getWindSpeed() == null ) {
    		return true;
    	}
    	if (weatherReport.getTemperatureDegrees() == null) {
    		return true;
    	}
    	return false;
    }
	
}
