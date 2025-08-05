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
    public WeatherReportSourceManager(WeatherStackAPI weatherStackAPI, OpenWeatherMapAPI openWeatherMapAPI) {
        this.weatherStackAPI = weatherStackAPI;
        this.openWeatherMapAPI = openWeatherMapAPI;
    }
    
    @Cacheable(value = "weatherCache", unless = "#result == null || #result.windSpeed == null || #result.temperatureDegrees == null")
    public WeatherReport getWeatherReport() {
    	
    	WeatherReport weatherReport = new WeatherReport();
    	
    	//Weather Stack API
    	weatherReport = weatherStackAPI.getWeatherReport();
    	if (weatherReport.hasValidData()) return weatherReport; 
    	
    	//Open Weather API
    	weatherReport = openWeatherMapAPI.getWeatherReport();
    	if(weatherReport.hasValidData()) return weatherReport;

    	//Can add other APIs here if needed...
    	return weatherReport;
    	
    }
	
}
