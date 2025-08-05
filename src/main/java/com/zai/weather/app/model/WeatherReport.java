package com.zai.weather.app.model;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@ToString
public class WeatherReport {

	private String windSpeed;
	private String temperatureDegrees;
	
	public boolean hasValidData() {
		return ObjectUtils.allNotNull(windSpeed, temperatureDegrees);
	}
	
}
