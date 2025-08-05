package com.zai.weather.app.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class WeatherReport {

	private String windSpeed;
	private String temperatureDegrees;
	
}
