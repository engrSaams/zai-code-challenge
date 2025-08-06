package com.zai.weather.app.service;

import com.zai.weather.app.model.WeatherReport;

public interface WeatherReportSource {

	public WeatherReport getWeatherReport(String city);
	public String buildUrl(String city);

}
