package com.zai.weather.app.service;

import com.zai.weather.app.model.WeatherReport;

public interface WeatherReportSource {

	public WeatherReport getWeatherReport();
	public String buildUrl();

}
