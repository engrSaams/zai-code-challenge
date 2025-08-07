package com.zai.weather.app.model;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@lombok.Generated
@Component
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WeatherReport {

	private String windSpeed;
	private String temperatureDegrees;
	
	public boolean hasValidData() {
		return ObjectUtils.allNotNull(windSpeed, temperatureDegrees);
	}
	
}
