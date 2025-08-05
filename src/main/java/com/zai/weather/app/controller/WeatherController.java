package com.zai.weather.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zai.weather.app.model.WeatherReport;
import com.zai.weather.app.service.WeatherService;

@RestController
@RequestMapping("${endpoint.url}")
public class WeatherController {
	
	private WeatherService weatherService;
	
    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
	
	@GetMapping(value = "/weather")
	public ResponseEntity<WeatherReport> weatherReport(@RequestParam String city) {
		return new ResponseEntity<WeatherReport>(weatherService.getWeatherReport(), HttpStatus.OK);
	}
	
}
