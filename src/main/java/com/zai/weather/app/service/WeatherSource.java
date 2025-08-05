package com.zai.weather.app.service;

import com.zai.weather.app.model.WeatherReport;

public interface WeatherSource {

	public WeatherReport perform();
	
}
